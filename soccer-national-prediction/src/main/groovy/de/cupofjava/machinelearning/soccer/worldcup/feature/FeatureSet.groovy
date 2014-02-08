package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import org.encog.ml.data.MLData
import org.encog.ml.data.MLDataSet
import org.encog.ml.data.basic.BasicMLData
import org.encog.ml.data.basic.BasicMLDataPair
import org.encog.ml.data.basic.BasicMLDataSet

import static groovyx.gpars.GParsPool.withPool

/**
 * @author fmueller
 */
class FeatureSet {

  List<Feature> features

  private int sizeOfFeatureSet = 3

  FeatureSet(Feature... features) {
    this.features = new LinkedList(Arrays.asList(features))
    for (Feature feature : features) {
      sizeOfFeatureSet += feature.getSize()
    }
  }

  MLDataSet computeDataSet(Collection<Match> matches) {
    withPool {
      new BasicMLDataSet(matches.parallel.map { match ->
        new BasicMLDataPair(computeInputData(match), computeIdealOutput(match))
      }.collection)
    }
  }

  MLData computeInputData(Match match) {
    double[] input = new double[sizeOfFeatureSet]
    int i = 0
    for (Feature feature : features) {
      for (double computedInput : feature.compute(match.getDate(), match.getHomeTeam(), match.getAwayTeam())) {
        input[i++] = computedInput
      }
    }
    input[i++] = 1 / match.getHomeWinOdds()
    input[i++] = 1 / match.getDrawOdds()
    input[i] = 1 / match.getAwayWinOdds()
    new BasicMLData(input)
  }

  private MLData computeIdealOutput(Match match) {
    double[] idealOutput = new double[3]
    idealOutput[0] = match.isHomeWin() ? 1.0 : 0.0
    idealOutput[1] = match.isDraw() ? 1.0 : 0.0
    idealOutput[2] = match.isAwayWin() ? 1.0 : 0.0
    new BasicMLData(idealOutput)
  }
}
