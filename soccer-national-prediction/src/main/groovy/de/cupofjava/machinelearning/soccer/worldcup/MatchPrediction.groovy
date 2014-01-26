package de.cupofjava.machinelearning.soccer.worldcup

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * @author fmueller
 */
@EqualsAndHashCode
@ToString
class MatchPrediction {

  Match match

  private double homeWinProbability
  private double drawProbability
  private double awayWinProbability

  MatchPrediction(Match match, double[] neuralNetworkOutput) {
    this.match = match
    this.homeWinProbability = neuralNetworkOutput[0]
    this.drawProbability = neuralNetworkOutput[1]
    this.awayWinProbability = neuralNetworkOutput[2]
  }

  boolean isHomeWinPredicted() {
    !isDrawPredicted() && !isAwayWinPredicted()
  }

  boolean isDrawPredicted() {
    drawProbability > homeWinProbability && drawProbability > awayWinProbability
  }

  boolean isAwayWinPredicted() {
    awayWinProbability > homeWinProbability && !isDrawPredicted()
  }

  boolean isCorrectHomeWin() {
    match.isHomeWin() && isHomeWinPredicted()
  }

  boolean isCorrectDraw() {
    match.isDraw() && isDrawPredicted()
  }

  boolean isCorrectAwayWin() {
    match.isAwayWin() && isAwayWinPredicted()
  }
}
