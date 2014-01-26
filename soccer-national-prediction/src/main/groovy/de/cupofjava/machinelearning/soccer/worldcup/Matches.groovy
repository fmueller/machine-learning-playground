package de.cupofjava.machinelearning.soccer.worldcup

import org.joda.time.LocalDate

/**
 * @author fmueller
 */
class Matches {

  private static final int LAST_MATCHES = 6

  private static final Set<Match> MATCHES = new HashSet<>()

  private static final Comparator<Match> BY_DATE_DESCENDING = new Comparator<Match>() {
    @Override
    int compare(Match match, Match otherMatch) {
      -1 * match.getDate().compareTo(otherMatch.getDate())
    }
  }

  static def storeAllMatches(Collection<Match> matches) {
    MATCHES.addAll(matches)
  }

  static Collection<Match> allMatches() {
    new HashSet<Match>(MATCHES)
  }

  static Collection<Match> allMatchesBefore(LocalDate matchDate, String team) {
    MATCHES.grep { matchDate.isAfter(it.getDate()) && it.hasPlayed(team) }
  }

  static Collection<Match> allMatchesBefore(LocalDate matchDate, String homeTeam, String awayTeam) {
    MATCHES.grep { matchDate.isAfter(it.getDate()) && it.hasPlayed(homeTeam) && it.hasPlayed(awayTeam) }
  }

  static Collection<Match> allHomeMatchesBefore(LocalDate matchDate, String homeTeam) {
    MATCHES.grep { matchDate.isAfter(it.getDate()) && it.isHomeTeam(homeTeam) }
  }

  static Collection<Match> allHomeMatchesBefore(LocalDate matchDate, String homeTeam, String awayTeam) {
    MATCHES.grep { matchDate.isAfter(it.getDate()) && it.isHomeTeam(homeTeam) && it.isAwayTeam(awayTeam) }
  }

  static Collection<Match> allAwayMatchesBefore(LocalDate matchDate, String awayTeam) {
    MATCHES.grep { matchDate.isAfter(it.getDate()) && it.isAwayTeam(awayTeam) }
  }

  static Collection<Match> lastMatchesBefore(LocalDate matchDate, String team) {
    takeUpTo(LAST_MATCHES, allMatchesBefore(matchDate, team).sort(BY_DATE_DESCENDING))
  }

  static Collection<Match> lastMatchesBefore(LocalDate matchDate, String homeTeam, String awayTeam) {
    takeUpTo(LAST_MATCHES, allMatchesBefore(matchDate, homeTeam, awayTeam).sort(BY_DATE_DESCENDING))
  }

  static Collection<Match> lastHomeMatchesBefore(LocalDate matchDate, String homeTeam) {
    takeUpTo(LAST_MATCHES, allHomeMatchesBefore(matchDate, homeTeam).sort(BY_DATE_DESCENDING))
  }

  static Collection<Match> lastHomeMatchesBefore(LocalDate matchDate, String homeTeam, String awayTeam) {
    takeUpTo(LAST_MATCHES, allHomeMatchesBefore(matchDate, homeTeam, awayTeam).sort(BY_DATE_DESCENDING))
  }

  static Collection<Match> lastAwayMatchesBefore(LocalDate matchDate, String awayTeam) {
    takeUpTo(LAST_MATCHES, allAwayMatchesBefore(matchDate, awayTeam).sort(BY_DATE_DESCENDING))
  }

  static double homeWinRatio() {
    100 * MATCHES.grep { it.isHomeWin() }.size() / (double) MATCHES.size()
  }

  static double drawRatio() {
    100 * MATCHES.grep { it.isDraw() }.size() / (double) MATCHES.size()
  }

  static double awayWinRatio() {
    100 * MATCHES.grep { it.isAwayWin() }.size() / (double) MATCHES.size()
  }

  private static Collection<Match> takeUpTo(int numberOfMatches, List<Match> matches) {
    matches.size() >= numberOfMatches ? matches.subList(0, numberOfMatches) : matches
  }
}
