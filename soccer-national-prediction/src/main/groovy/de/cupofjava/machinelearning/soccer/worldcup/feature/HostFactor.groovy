package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class HostFactor implements Feature {

  /**
   * {@inheritDoc}
   */
  @Override
  int getSize() {
    6
  }

  /**
   * {@inheritDoc}
   */
  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] hostFactor = new double[getSize()]
    hostFactor[0] = hostFactorForMatches(
        Matches.allHomeMatchesBefore(matchDate, homeTeam),
        Matches.allAwayMatchesBefore(matchDate, homeTeam))
    hostFactor[1] = hostFactorForMatches(
        Matches.lastHomeMatchesBefore(matchDate, homeTeam),
        Matches.lastAwayMatchesBefore(matchDate, homeTeam))

    hostFactor[2] = hostFactorForMatches(
        Matches.allAwayMatchesBefore(matchDate, awayTeam),
        Matches.allHomeMatchesBefore(matchDate, awayTeam))
    hostFactor[3] = hostFactorForMatches(
        Matches.lastAwayMatchesBefore(matchDate, awayTeam),
        Matches.lastHomeMatchesBefore(matchDate, awayTeam))

    hostFactor[4] = hostFactor[0] - hostFactor[2]
    hostFactor[5] = hostFactor[1] - hostFactor[3]

    hostFactor
  }

  private double hostFactorForMatches(Collection<Match> homeMatches, Collection<Match> awayMatches) {
    double possibleHomePoints = Math.max(1.0, homeMatches.size() * 3.0)
    double possibleAwayPoints = Math.max(1.0, awayMatches.size() * 3.0)

    double homePointsRatio = (homeMatches.grep { it.isHomeWin() }.size() * 3 + homeMatches.grep { it.isDraw() }.size()) / possibleHomePoints
    double awayPointsRatio = (awayMatches.grep { it.isAwayWin() }.size() * 3 + awayMatches.grep { it.isDraw() }.size()) / possibleAwayPoints

    (homePointsRatio - awayPointsRatio) / awayPointsRatio > 0.0 ? awayPointsRatio : 1.0
  }
}
