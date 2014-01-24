package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import org.encog.ml.data.MLData
import org.encog.ml.data.MLDataSet
import org.encog.ml.data.basic.BasicMLData
import org.encog.ml.data.basic.BasicMLDataSet

/**
 * @author fmueller
 */
class FeatureSet {

  List<Feature> features

  private int sizeOfFeatureSet

  FeatureSet(Feature... features) {
    this.features = new LinkedList(Arrays.asList(features))
    for (Feature feature : features) {
      sizeOfFeatureSet += feature.getSize()
    }
  }

  MLDataSet computeDataSet(Collection<Match> matches) {
    def dataSet = new BasicMLDataSet()
    for (Match match : matches) {
      dataSet.add(computeInputData(match), computeIdealOutput(match))
    }
    dataSet
  }

  MLData computeInputData(Match match) {
    double[] input = new double[sizeOfFeatureSet]
    int i = 0
    for (Feature feature : features) {
      for (double computedInput : feature.compute(match.getDate(), match.getHomeTeam(), match.getAwayTeam())) {
        input[i++] = computedInput
      }
    }
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
