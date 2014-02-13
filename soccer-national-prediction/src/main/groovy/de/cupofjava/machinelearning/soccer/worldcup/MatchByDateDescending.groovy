package de.cupofjava.machinelearning.soccer.worldcup

/**
 * @author fmueller
 */
class MatchByDateDescending implements Comparator<Match> {

  @Override
  int compare(Match match, Match otherMatch) {
    -1 * match.getDate().compareTo(otherMatch.getDate())
  }
}
