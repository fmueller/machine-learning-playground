package de.cupofjava.machinelearning.soccer.worldcup

import groovy.transform.EqualsAndHashCode

/**
 * @author fmueller
 */
@EqualsAndHashCode
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
    homeWinProbability > drawProbability && homeWinProbability > awayWinProbability
  }

  boolean isDrawPredicted() {
    drawProbability > homeWinProbability && drawProbability > awayWinProbability
  }

  boolean isAwayWinPredicted() {
    awayWinProbability > homeWinProbability && awayWinProbability > homeWinProbability
  }
}