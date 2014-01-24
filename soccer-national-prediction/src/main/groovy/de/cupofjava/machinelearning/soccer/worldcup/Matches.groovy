package de.cupofjava.machinelearning.soccer.worldcup

import org.joda.time.LocalDate

/**
 * @author fmueller
 */
class Matches {

  private static Set<Match> matches = new HashSet<>()

  private static Comparator<Match> BY_DATE_DESCENDING = new Comparator<Match>() {
    @Override
    int compare(Match match, Match otherMatch) {
      -1 * match.getDate().compareTo(otherMatch.getDate())
    }
  }

  static def storeAllMatches(Collection<Match> matches) {
    Matches.matches.addAll(matches)
  }

  static Collection<Match> allMatches() {
    new HashSet<Match>(matches)
  }

  static Collection<Match> allMatchesBefore(LocalDate matchDate, String team) {
    matches.grep { matchDate.isAfter(it.getDate()) && it.hasPlayed(team) }
  }

  static Collection<Match> allHomeMatchesBefore(LocalDate matchDate, String homeTeam) {
    matches.grep { matchDate.isAfter(it.getDate()) && it.isHomeTeam(homeTeam) }
  }

  static Collection<Match> allAwayMatchesBefore(LocalDate matchDate, String awayTeam) {
    matches.grep { matchDate.isAfter(it.getDate()) && it.isAwayTeam(awayTeam) }
  }

  static Collection<Match> lastMatchesBefore(int numberOfMatches, LocalDate matchDate, String team) {
    takeUpTo(numberOfMatches, allMatchesBefore(matchDate, team).sort(BY_DATE_DESCENDING))
  }

  static Collection<Match> lastHomeMatchesBefore(int numberOfMatches, LocalDate matchDate, String homeTeam) {
    takeUpTo(numberOfMatches, allHomeMatchesBefore(matchDate, homeTeam).sort(BY_DATE_DESCENDING))
  }

  static Collection<Match> lastAwayMatchesBefore(int numberOfMatches, LocalDate matchDate, String awayTeam) {
    takeUpTo(numberOfMatches, allAwayMatchesBefore(matchDate, awayTeam).sort(BY_DATE_DESCENDING))
  }

  static double homeWinRatio() {
    100 * matches.grep { it.isHomeWin() }.size() / (double) matches.size()
  }

  private static Collection<Match> takeUpTo(int numberOfMatches, List<Match> matches) {
    if (matches.size() < numberOfMatches) {
      matches
    } else {
      matches.subList(0, numberOfMatches)
    }
  }
}
