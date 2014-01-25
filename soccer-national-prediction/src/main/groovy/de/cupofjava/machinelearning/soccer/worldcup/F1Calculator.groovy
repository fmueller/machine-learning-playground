package de.cupofjava.machinelearning.soccer.worldcup

import java.math.RoundingMode

/**
 * @author fmueller
 */
class F1Calculator {

  private final Collection<MatchPrediction> predictions

  F1Calculator(Collection<MatchPrediction> predictions) {
    this.predictions = predictions
  }

  double computeDefault() {
    double homeWin = f1Score(
        { it.isHomeWinPredicted() && it.getMatch().isHomeWin() },
        { it.isHomeWinPredicted() && !it.getMatch().isHomeWin() },
        { !it.isHomeWinPredicted() && it.getMatch().isHomeWin() })
    double draw = f1Score(
        { it.isDrawPredicted() && it.getMatch().isDraw() },
        { it.isDrawPredicted() && !it.getMatch().isDraw() },
        { !it.isDrawPredicted() && it.getMatch().isDraw() })
    double awayWin = f1Score(
        { it.isAwayWinPredicted() && it.getMatch().isAwayWin() },
        { it.isAwayWinPredicted() && !it.getMatch().isAwayWin() },
        { !it.isAwayWinPredicted() && it.getMatch().isAwayWin() })

    roundToFiveDecimalPlaces((homeWin + draw + awayWin) / 3.0)
  }

  double computeWeighted() {
    double homeWin = f1Score(
        { it.isHomeWinPredicted() && it.getMatch().isHomeWin() },
        { it.isHomeWinPredicted() && !it.getMatch().isHomeWin() },
        { !it.isHomeWinPredicted() && it.getMatch().isHomeWin() })
    double draw = f1Score(
        { it.isDrawPredicted() && it.getMatch().isDraw() },
        { it.isDrawPredicted() && !it.getMatch().isDraw() },
        { !it.isDrawPredicted() && it.getMatch().isDraw() })
    double awayWin = f1Score(
        { it.isAwayWinPredicted() && it.getMatch().isAwayWin() },
        { it.isAwayWinPredicted() && !it.getMatch().isAwayWin() },
        { !it.isAwayWinPredicted() && it.getMatch().isAwayWin() })

    double homeWinRatio = predictions.grep { it.getMatch().isHomeWin() }.size() / (double) predictions.size()
    double drawRatio = predictions.grep { it.getMatch().isDraw() }.size() / (double) predictions.size()
    double awayWinRatio = predictions.grep { it.getMatch().isAwayWin() }.size() / (double) predictions.size()
    roundToFiveDecimalPlaces(homeWin * homeWinRatio + draw * drawRatio + awayWin * awayWinRatio)
  }

  private double f1Score(Closure truePositives, Closure falsePositives, Closure falseNegatives) {
    2 * ((precision(truePositives, falsePositives) * recall(truePositives, falseNegatives)) /
        (precision(truePositives, falsePositives) + recall(truePositives, falseNegatives)))
  }

  private double precision(Closure truePositives, Closure falsePositives) {
    int truePositivesCount = predictions.grep(truePositives).size()
    int falsePositivesCount = predictions.grep(falsePositives).size()
    truePositivesCount / (double) (truePositivesCount + falsePositivesCount)
  }

  private double recall(Closure truePositives, Closure falseNegatives) {
    int truePositivesCount = predictions.grep(truePositives).size()
    int falseNegativesCount = predictions.grep(falseNegatives).size()
    truePositivesCount / (double) (truePositivesCount + falseNegativesCount)
  }

  private double roundToFiveDecimalPlaces(double value) {
    new BigDecimal(value).setScale(5, RoundingMode.HALF_UP).doubleValue()
  }
}
