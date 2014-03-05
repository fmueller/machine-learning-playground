package de.cupofjava.machinelearning.soccer.worldcup

import com.google.common.base.Charsets
import com.google.common.io.Resources
import de.cupofjava.machinelearning.soccer.worldcup.feature.*
import groovy.util.logging.Slf4j
import org.encog.engine.network.activation.ActivationLinear
import org.encog.engine.network.activation.ActivationTANH
import org.encog.ml.data.MLData
import org.encog.ml.data.MLDataPair
import org.encog.ml.data.MLDataSet
import org.encog.ml.train.MLTrain
import org.encog.ml.train.strategy.end.EarlyStoppingStrategy
import org.encog.ml.train.strategy.end.EndIterationsStrategy
import org.encog.ml.train.strategy.end.EndMaxErrorStrategy
import org.encog.neural.networks.BasicNetwork
import org.encog.neural.networks.layers.BasicLayer
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation
import org.encog.util.concurrency.EngineConcurrency
import weka.classifiers.meta.MultiClassClassifier
import weka.classifiers.trees.RandomForest
import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances

import static groovyx.gpars.GParsPool.withPool

/**
 * This project is a predictor for matches between national soccer teams.
 * For that, a neural network is trained and evaluated on a test data set.
 * Afterwards the performance of the bookmakers is evaluated on this
 * test data set.
 *
 * @author fmueller
 */
@Slf4j
class PredictMatchesMain {

  private static double TEST_DATA_RATIO = 0.2
  private static double VALIDATION_DATA_RATIO = 0.2

  private static double MAX_ERROR_RATE = 0.1
  private static int MAX_ITERATIONS = 1000

  private static double HIDDEN_LAYER_RATIO = 0.667

  static void main(args) {
    log.info("Loading match data...")
    Matches.storeAllMatches(MatchParser.parseMatches(
        Resources.toString(Resources.getResource("soccerData.csv"), Charsets.UTF_8)))
    List<Match> matches = new LinkedList<>(Matches.allMatches())
    log.info("Loaded {} matches", matches.size())

    int testDataSetSize = (int) Math.round(matches.size() * TEST_DATA_RATIO)
    int validationDataSetSize = (int) Math.round(matches.size() * VALIDATION_DATA_RATIO)

    log.info("Splitting data into training, validation and test data set...")
    def testMatches = chooseRandomMatches(matches, testDataSetSize)
    matches.removeAll(testMatches)
    def validationMatches = chooseRandomMatches(matches, validationDataSetSize)
    matches.removeAll(validationMatches)
    def trainingMatches = new HashSet<>(matches)

    def featureSet = new FeatureSet(new HostFactor(),
        new GoalDifferences(), new GoalAverages(),
        new MatchStatistics(), new MatchStatisticsDifferences(),
        new HeadToHead())

    log.info("Computing features for training data set...")
    def trainingData = featureSet.computeDataSet(trainingMatches)
    def wekaTrainingData = toWekaInstances(trainingData)

    log.info("Computing features for cross-validation data set...")
    def validationData = featureSet.computeDataSet(validationMatches)

    log.info("Start training of neural network (Encog)...")
    def network = createNeuralNetwork(trainingData)
    trainNetwork(network, trainingData, validationData)

    log.info("Start training of random forest...")
    def randomForest = new RandomForest()
    randomForest.setNumTrees(100)
    randomForest.buildClassifier(wekaTrainingData)

    log.info("Start training of multi class classifier (logistic)...")
    def multiClassLogistic = new MultiClassClassifier()
    multiClassLogistic.buildClassifier(wekaTrainingData)

    log.info("Evaluating classifiers...")
    testNetwork(network, testMatches, featureSet)
    testWekaClassifier("Random Forest", randomForest, testMatches, featureSet)
    testWekaClassifier("Multi Class Classifier (Logistic)", multiClassLogistic, testMatches, featureSet)
    testBookie(testMatches)

    EngineConcurrency.getInstance().shutdown(2000L)
  }

  private static Instances toWekaInstances(MLDataSet dataPairs) {
    def classificationResult = new FastVector(3)
    classificationResult.addElement("HOME")
    classificationResult.addElement("DRAW")
    classificationResult.addElement("AWAY")

    def numberColumns = dataPairs.getInputSize() + 1
    def attributes = new FastVector(numberColumns)
    attributes.addElement(new Attribute("classification", classificationResult))
    for (int i = 0; i < dataPairs.getInputSize(); i++) {
      attributes.addElement(new Attribute("input-" + i))
    }

    Instances instances = new Instances("WekaInstances", attributes, dataPairs.size())
    instances.setClassIndex(0)

    for (MLDataPair data : dataPairs) {
      def ideal = data.getIdealArray()
      def idealOutput = "HOME"
      if (ideal[1] > ideal[0] && ideal[1] > ideal[2]) {
        idealOutput = "DRAW"
      } else if (ideal[2] > ideal[0] && ideal[2] > ideal[1]) {
        idealOutput = "AWAY"
      }

      def instance = new Instance(numberColumns)
      instance.setValue((Attribute) attributes.elementAt(0), idealOutput)
      for (int i = 0; i < data.getInputArray().length; i++) {
        instance.setValue((Attribute) attributes.elementAt(i + 1), data.getInputArray()[i])
      }
      instances.add(instance)
    }

    instances
  }

  private static BasicNetwork createNeuralNetwork(MLDataSet trainingData) {
    BasicNetwork network = new BasicNetwork()
    network.addLayer(new BasicLayer(new ActivationLinear(), false, trainingData.getInputSize()))
    network.addLayer(new BasicLayer(new ActivationTANH(), true, (int) Math.round(trainingData.getInputSize() * HIDDEN_LAYER_RATIO)))
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
    log.info("Finished training after {} iterations with best error rate of {}.", epoch, best)
  }

  private static void testWekaClassifier(classifierName, classifier, testMatches, featureSet) {
    log.info("")
    log.info(classifierName + " Performance:")
    log.info("")
    def instances = toWekaInstances(featureSet.computeDataSet(testMatches))
    evaluatePredictions(testMatches, { match ->
      MLData input = featureSet.computeInputData(match)
      Instance instance = new Instance(input.size() + 1)
      instance.setDataset(instances)

      def idealOutput = "HOME"
      if (match.isDraw()) {
        idealOutput = "DRAW"
      } else if (match.isAwayWin()) {
        idealOutput = "AWAY"
      }
      instance.setValue(instances.attribute(0), idealOutput)

      for (int i = 0; i < input.size(); i++) {
        instance.setValue(instances.attribute(i + 1), input.getData(i))
      }

      double[] output = classifier.distributionForInstance(instance)
      new MatchPrediction(match, output)
    })
  }

  private static void testNetwork(network, testMatches, featureSet) {
    log.info("")
    log.info("Neural Network Performance:")
    log.info("")
    evaluatePredictions(testMatches, { match ->
      MLData input = featureSet.computeInputData(match)
      double[] output = network.compute(input).getData()
      new MatchPrediction(match, output)
    })
  }

  private static void testBookie(testMatches) {
    log.info("")
    log.info("Bookie Performance:")
    log.info("")
    evaluatePredictions(testMatches, { it.getBookmakerPrediction() })
  }

  private static void evaluatePredictions(testMatches, predictMatch) {
    def predictions = withPool { testMatches.parallel.map(predictMatch).collection }

    def correctHomeWin = 0
    def correctDraw = 0
    def correctAwayWin = 0

    def predictedHomeWin = 0
    def predictedDraw = 0
    def predictedAwayWin = 0

    for (MatchPrediction prediction : predictions) {
      if (prediction.isCorrectHomeWin()) {
        correctHomeWin++
      } else if (prediction.isCorrectDraw()) {
        correctDraw++
      } else if (prediction.isCorrectAwayWin()) {
        correctAwayWin++
      }

      if (prediction.isHomeWinPredicted()) {
        predictedHomeWin++
      } else if (prediction.isDrawPredicted()) {
        predictedDraw++
      } else if (prediction.isAwayWinPredicted()) {
        predictedAwayWin++
      }
    }

    def matchesCount = (double) testMatches.size()
    def correct = correctHomeWin + correctDraw + correctAwayWin
    def homeWinCount = testMatches.grep { it.isHomeWin() }.size()
    def drawCount = testMatches.grep { it.isDraw() }.size()
    def awayWinCount = testMatches.grep { it.isAwayWin() }.size()

    log.info("Home win, draw, away win ratio on whole data set: {}% / {}% / {}%",
        String.format("%.2f", Matches.homeWinRatio()),
        String.format("%.2f", Matches.drawRatio()),
        String.format("%.2f", Matches.awayWinRatio()))
    log.info("Home win, draw, away win ratio of predictions: {}% / {}% / {}%",
        String.format("%.2f", 100 * predictedHomeWin / matchesCount),
        String.format("%.2f", 100 * predictedDraw / matchesCount),
        String.format("%.2f", 100 * predictedAwayWin / matchesCount))

    log.info("Predicted home wins: {}", predictedHomeWin)
    log.info("Predicted draws: {}", predictedDraw)
    log.info("Predicted away wins: {}", predictedAwayWin)

    log.info("Correct matches: {} of {}", correct, testMatches.size())
    log.info("Correct home wins: {} of {}", correctHomeWin, homeWinCount)
    log.info("Correct draws: {} of {}", correctDraw, drawCount)
    log.info("Correct away wins: {} of {}", correctAwayWin, awayWinCount)

    log.info("Accuracy on test data: {}%", String.format("%.2f", 100 * correct / matchesCount))

    def f1Calculator = new F1Calculator(predictions)
    log.info("F1 Score: {}", f1Calculator.computeDefault())
    log.info("Weighted F1 Score: {}", f1Calculator.computeWeighted())
  }

  private static Collection<Match> chooseRandomMatches(matches, numberOfMatches) {
    Collections.sort(matches, new MatchByDateDescending())
    Set<Match> choosenMatches = new HashSet<>()
    choosenMatches.addAll(matches.subList(0, numberOfMatches))
    choosenMatches
  }
}
