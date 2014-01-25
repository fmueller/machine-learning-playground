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

  Match(LocalDate date, String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
    this.date = date
    this.homeTeam = homeTeam
    this.awayTeam = awayTeam
    this.homeGoals = homeGoals
    this.awayGoals = awayGoals
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
