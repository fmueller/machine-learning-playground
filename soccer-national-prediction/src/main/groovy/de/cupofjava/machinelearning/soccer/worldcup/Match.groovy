package de.cupofjava.machinelearning.soccer.worldcup

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
@EqualsAndHashCode
@ToString
class Match {

  LocalDate date
  String homeTeam
  String awayTeam
  int homeGoals
  int awayGoals

  double homeWinOdds
  double drawOdds
  double awayWinOdds

  Match(LocalDate date, String homeTeam, String awayTeam,
        int homeGoals, int awayGoals,
        double homeWinOdds, double drawOdds, double awayWinOdds) {
    this.date = date
    this.homeTeam = homeTeam
    this.awayTeam = awayTeam
    this.homeGoals = homeGoals
    this.awayGoals = awayGoals
    this.homeWinOdds = homeWinOdds
    this.drawOdds = drawOdds
    this.awayWinOdds = awayWinOdds
  }

  MatchPrediction getBookmakerPrediction() {
    new MatchPrediction(this, 1.0 / homeWinOdds, 1.0 / drawOdds, 1.0 / awayWinOdds)
  }

  boolean isHomeTeam(String team) {
    homeTeam.equalsIgnoreCase(team)
  }

  boolean isAwayTeam(String team) {
    awayTeam.equalsIgnoreCase(team)
  }

  boolean hasPlayed(String team) {
    isHomeTeam(team) || isAwayTeam(team)
  }

  boolean isHomeWin() {
    homeGoals > awayGoals
  }

  boolean isDraw() {
    homeGoals == awayGoals
  }

  boolean isAwayWin() {
    homeGoals < awayGoals
  }
}
