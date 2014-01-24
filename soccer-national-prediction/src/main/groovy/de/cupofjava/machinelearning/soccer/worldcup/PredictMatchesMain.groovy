package de.cupofjava.machinelearning.soccer.worldcup

import com.google.common.base.Charsets
import com.google.common.io.Resources
import de.cupofjava.machinelearning.soccer.worldcup.feature.FeatureSet
import de.cupofjava.machinelearning.soccer.worldcup.feature.GoalAverages
import de.cupofjava.machinelearning.soccer.worldcup.feature.GoalDifferences
import de.cupofjava.machinelearning.soccer.worldcup.feature.HostFactor
import groovy.util.logging.Slf4j
import org.encog.engine.network.activation.ActivationLinear
import org.encog.engine.network.activation.ActivationTANH
import org.encog.ml.data.MLData
import org.encog.ml.data.MLDataSet
import org.encog.ml.train.MLTrain
import org.encog.ml.train.strategy.end.EarlyStoppingStrategy
import org.encog.ml.train.strategy.end.EndIterationsStrategy
import org.encog.ml.train.strategy.end.EndMaxErrorStrategy
import org.encog.neural.networks.BasicNetwork
import org.encog.neural.networks.layers.BasicLayer
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation
import org.encog.util.concurrency.EngineConcurrency

/**
 * TODO javadoc
 *
 * @author fmueller
 */
@Slf4j
class PredictMatchesMain {

  private static double TRAINING_DATA_RATIO = 0.6
  private static double VALIDATION_DATA_RATIO = 0.2

  private static double MAX_ERROR_RATE = 0.1
  private static int MAX_ITERATIONS = 5000

  static void main(args) {
    String csv = Resources.toString(Resources.getResource("soccerData.csv"), Charsets.UTF_8)
    Matches.storeAllMatches(MatchParser.parseMatches(csv))
    List<Match> matches = new LinkedList<>(Matches.allMatches())
    log.info("Loaded {} matches", matches.size())

    int trainingDataSetSize = (int) Math.round(matches.size() * TRAINING_DATA_RATIO)
    int validationDataSetSize = (int) Math.round(matches.size() * VALIDATION_DATA_RATIO)

    log.info("Training data set size: {}", trainingDataSetSize)
    log.info("Validation data set size: {}", validationDataSetSize)

    log.info("Splitting data into training, validation and test data set...")
    // TODO split match data by exact home win/draw/away win ratio
    Collection<Match> trainingMatches = chooseRandomMatches(matches, trainingDataSetSize)
    matches.removeAll(trainingMatches)
    Collection<Match> validationMatches = chooseRandomMatches(matches, validationDataSetSize)
    matches.removeAll(validationMatches)

    log.info("Computing features...")
    FeatureSet featureSet = new FeatureSet(new HostFactor(), new GoalDifferences(), new GoalAverages())
    MLDataSet trainingData = featureSet.computeDataSet(trainingMatches)
    MLDataSet validationData = featureSet.computeDataSet(validationMatches)

    log.info("Start training...")
    BasicNetwork network = createNeuralNetwork(trainingData)
    trainNetwork(network, trainingData, validationData)

    log.info("Evaluating network performance on test data...")
    testNetwork(network, new HashSet<>(matches), featureSet)

    EngineConcurrency.getInstance().shutdown(2000L)
  }

  static void testNetwork(BasicNetwork network, HashSet<Match> testMatches, FeatureSet featureSet) {
    int correct = 0
    int correctHomeWin = 0
    int correctDraw = 0
    int correctAwayWin = 0

    for (Match match : testMatches) {
      MLData input = featureSet.computeInputData(match)
      double[] output = network.compute(input).getData()
      if (match.isHomeWin() && output[0] > output[1] && output[0] > output[2]) {
        correct++
        correctHomeWin++
      } else if (match.isDraw() && output[1] > output[0] && output[1] > output[2]) {
        correct++
        correctDraw++
      } else if (match.isAwayWin() && output[2] > output[0] && output[2] > output[1]) {
        correct++
        correctAwayWin++
      }
    }

    log.info("Correct matches: {} of {}", correct, testMatches.size())
    log.info("Correct home wins: {}", correctHomeWin)
    log.info("Correct draws: {}", correctDraw)
    log.info("Correct away wins: {}", correctAwayWin)

    log.info("Home win frequency on test data set: {}%", String.format("%.2f", 100 * testMatches.grep { it.isHomeWin() }.size() / (double) testMatches.size()))
    log.info("Home win frequency on whole data set: {}%", String.format("%.2f", Matches.homeWinFrequency()))

    log.info("Accuracy on test data: {}%", String.format("%.2f", 100 * correct / (double) testMatches.size()))
    // TODO calculate F1 score
  }

  private static BasicNetwork createNeuralNetwork(MLDataSet trainingData) {
    BasicNetwork network = new BasicNetwork()
    network.addLayer(new BasicLayer(new ActivationLinear(), false, trainingData.getInputSize()))
    network.addLayer(new BasicLayer(new ActivationTANH(), true, trainingData.getInputSize()))
    network.addLayer(new BasicLayer(new ActivationLinear(), true, trainingData.getIdealSize()))
    network.getStructure().finalizeStructure()
    network.reset()
    network
  }

  private static void trainNetwork(BasicNetwork network, MLDataSet trainingData, MLDataSet validationData) {
    MLTrain trainer = new ResilientPropagation(network, trainingData);
    trainer.addStrategy(new EndMaxErrorStrategy(MAX_ERROR_RATE));
    trainer.addStrategy(new EndIterationsStrategy(MAX_ITERATIONS));
    trainer.addStrategy(new EarlyStoppingStrategy(validationData, trainingData));

    double best = Double.MAX_VALUE
    int epoch = 0
    while (!trainer.isTrainingDone()) {
      trainer.iteration()
      double error = network.calculateError(trainingData)
      if (error < best) {
        best = error
        log.info("Epoch: {}, Error: {}", epoch, error)
      }
      epoch++
    }
    trainer.finishTraining()
    log.info("Finished training after {} iteration with best error rate of {}.", epoch, best)
  }

  private static Collection<Match> chooseRandomMatches(List<Match> matches, int numberOfMatches) {
    Collections.shuffle(matches)
    new HashSet<>(matches.subList(0, numberOfMatches))
  }
}