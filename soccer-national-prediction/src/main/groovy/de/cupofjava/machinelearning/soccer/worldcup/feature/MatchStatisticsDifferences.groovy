package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class MatchStatisticsDifferences implements Feature {

  /**
   * {@inheritDoc}
   */
  @Override
  int getSize() {
    16
  }

  /**
   * {@inheritDoc}
   */
  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] statisticsDiff = new double[getSize()]

    def allMatchesOfHomeTeam = Matches.allMatchesBefore(matchDate, homeTeam)
    def allMatchesOfAwayTeam = Matches.allMatchesBefore(matchDate, awayTeam)
    statisticsDiff[0] = playedMatches(allMatchesOfHomeTeam) - playedMatches(allMatchesOfAwayTeam)
    statisticsDiff[1] = countMatches(allMatchesOfHomeTeam, { it.isHomeWin() }) - countMatches(allMatchesOfAwayTeam, { it.isHomeWin() })
    statisticsDiff[2] = countMatches(allMatchesOfHomeTeam, { it.isDraw() }) - countMatches(allMatchesOfAwayTeam, { it.isDraw() })
    statisticsDiff[3] = countMatches(allMatchesOfHomeTeam, { it.isAwayWin() }) - countMatches(allMatchesOfAwayTeam, { it.isAwayWin() })

    def lastMatchesOfHomeTeam = Matches.lastMatchesBefore(matchDate, homeTeam)
    def lastMatchesOfAwayTeam = Matches.lastMatchesBefore(matchDate, awayTeam)
    statisticsDiff[4] = playedMatches(lastMatchesOfHomeTeam) - playedMatches(lastMatchesOfAwayTeam)
    statisticsDiff[5] = countMatches(lastMatchesOfHomeTeam, { it.isHomeWin() }) - countMatches(lastMatchesOfAwayTeam, { it.isHomeWin() })
    statisticsDiff[6] = countMatches(lastMatchesOfHomeTeam, { it.isDraw() }) - countMatches(lastMatchesOfAwayTeam, { it.isDraw() })
    statisticsDiff[7] = countMatches(lastMatchesOfHomeTeam, { it.isAwayWin() }) - countMatches(lastMatchesOfAwayTeam, { it.isAwayWin() })

    def allHomeMatchesOfHomeTeam = Matches.allHomeMatchesBefore(matchDate, homeTeam)
    def allAwayMatchesOfAwayTeam = Matches.allAwayMatchesBefore(matchDate, awayTeam)
    statisticsDiff[8] = playedMatches(allHomeMatchesOfHomeTeam) - playedMatches(allAwayMatchesOfAwayTeam)
    statisticsDiff[9] = countMatches(allHomeMatchesOfHomeTeam, { it.isHomeWin() }) - countMatches(allAwayMatchesOfAwayTeam, { it.isAwayWin() })
    statisticsDiff[10] = countMatches(allHomeMatchesOfHomeTeam, { it.isDraw() }) - countMatches(allAwayMatchesOfAwayTeam, { it.isDraw() })
    statisticsDiff[11] = countMatches(allHomeMatchesOfHomeTeam, { it.isAwayWin() }) - countMatches(allAwayMatchesOfAwayTeam, { it.isHomeWin() })

    def lastHomeMatchesOfHomeTeam = Matches.lastHomeMatchesBefore(matchDate, homeTeam)
    def lastAwayMatchesOfAwayTeam = Matches.lastAwayMatchesBefore(matchDate, awayTeam)
    statisticsDiff[12] = playedMatches(lastHomeMatchesOfHomeTeam) - playedMatches(lastAwayMatchesOfAwayTeam)
    statisticsDiff[13] = countMatches(lastHomeMatchesOfHomeTeam, { it.isHomeWin() }) - countMatches(lastAwayMatchesOfAwayTeam, { it.isAwayWin() })
    statisticsDiff[14] = countMatches(lastHomeMatchesOfHomeTeam, { it.isDraw() }) - countMatches(lastAwayMatchesOfAwayTeam, { it.isDraw() })
    statisticsDiff[15] = countMatches(lastHomeMatchesOfHomeTeam, { it.isAwayWin() }) - countMatches(lastAwayMatchesOfAwayTeam, { it.isHomeWin() })

    statisticsDiff
  }

  private double playedMatches(matches) {
    matches.size() / 100.0
  }

  private double countMatches(matches, typeOfMatch) {
    matches.count(typeOfMatch) / Math.max(1.0, (double) matches.size())
  }
}
