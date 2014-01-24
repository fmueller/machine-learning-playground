package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class HostFactor implements Feature {

  @Override
  int getSize() {
    2
  }

  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] hostFactor = new double[getSize()]
    hostFactor[0] = hostFactorForMatches(
        Matches.allHomeMatchesBefore(matchDate, homeTeam),
        Matches.allAwayMatchesBefore(matchDate, homeTeam))
    hostFactor[1] = hostFactorForMatches(
        Matches.lastHomeMatchesBefore(matchDate, homeTeam),
        Matches.lastAwayMatchesBefore(matchDate, homeTeam))
    hostFactor
  }

  private double hostFactorForMatches(Collection<Match> homeMatches, Collection<Match> awayMatches) {
    double possibleHomePoints = Math.max(1.0, homeMatches.size() * 3.0)
    double possibleAwayPoints = Math.max(1.0, awayMatches.size() * 3.0)

    double homePointsRatio = (homeMatches.grep { it.isHomeWin() }.size() * 3 + homeMatches.grep { it.isDraw() }.size()) / possibleHomePoints
    double awayPointsRatio = (awayMatches.grep { it.isAwayWin() }.size() * 3 + awayMatches.grep { it.isDraw() }.size()) / possibleAwayPoints

    homePointsRatio - awayPointsRatio
  }
}
