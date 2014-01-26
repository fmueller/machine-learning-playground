package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class MatchStatistics implements Feature {

  @Override
  int getSize() {
    32
  }

  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] matchStatistics = new double[getSize()]

    def allMatchesOfHomeTeam = Matches.allMatchesBefore(matchDate, homeTeam)
    matchStatistics[0] = playedMatches(allMatchesOfHomeTeam)
    matchStatistics[1] = countMatches(allMatchesOfHomeTeam, { it.isHomeWin() })
    matchStatistics[2] = countMatches(allMatchesOfHomeTeam, { it.isDraw() })
    matchStatistics[3] = countMatches(allMatchesOfHomeTeam, { it.isAwayWin() })

    def allMatchesOfAwayTeam = Matches.allMatchesBefore(matchDate, awayTeam)
    matchStatistics[4] = playedMatches(allMatchesOfAwayTeam)
    matchStatistics[5] = countMatches(allMatchesOfAwayTeam, { it.isHomeWin() })
    matchStatistics[6] = countMatches(allMatchesOfAwayTeam, { it.isDraw() })
    matchStatistics[7] = countMatches(allMatchesOfAwayTeam, { it.isAwayWin() })

    def lastMatchesOfHomeTeam = Matches.lastMatchesBefore(matchDate, homeTeam)
    matchStatistics[8] = playedMatches(lastMatchesOfHomeTeam)
    matchStatistics[9] = countMatches(lastMatchesOfHomeTeam, { it.isHomeWin() })
    matchStatistics[10] = countMatches(lastMatchesOfHomeTeam, { it.isDraw() })
    matchStatistics[11] = countMatches(lastMatchesOfHomeTeam, { it.isAwayWin() })

    def lastMatchesOfAwayTeam = Matches.lastMatchesBefore(matchDate, awayTeam)
    matchStatistics[12] = playedMatches(lastMatchesOfAwayTeam)
    matchStatistics[13] = countMatches(lastMatchesOfAwayTeam, { it.isHomeWin() })
    matchStatistics[14] = countMatches(lastMatchesOfAwayTeam, { it.isDraw() })
    matchStatistics[15] = countMatches(lastMatchesOfAwayTeam, { it.isAwayWin() })

    def allHomeMatchesOfHomeTeam = Matches.allHomeMatchesBefore(matchDate, homeTeam)
    matchStatistics[16] = playedMatches(allHomeMatchesOfHomeTeam)
    matchStatistics[17] = countMatches(allHomeMatchesOfHomeTeam, { it.isHomeWin() })
    matchStatistics[18] = countMatches(allHomeMatchesOfHomeTeam, { it.isDraw() })
    matchStatistics[19] = countMatches(allHomeMatchesOfHomeTeam, { it.isAwayWin() })

    def allAwayMatchesOfAwayTeam = Matches.allAwayMatchesBefore(matchDate, awayTeam)
    matchStatistics[20] = playedMatches(allAwayMatchesOfAwayTeam)
    matchStatistics[21] = countMatches(allAwayMatchesOfAwayTeam, { it.isHomeWin() })
    matchStatistics[22] = countMatches(allAwayMatchesOfAwayTeam, { it.isDraw() })
    matchStatistics[23] = countMatches(allAwayMatchesOfAwayTeam, { it.isAwayWin() })

    def lastHomeMatchesOfHomeTeam = Matches.lastHomeMatchesBefore(matchDate, homeTeam)
    matchStatistics[24] = playedMatches(lastHomeMatchesOfHomeTeam)
    matchStatistics[25] = countMatches(lastHomeMatchesOfHomeTeam, { it.isHomeWin() })
    matchStatistics[26] = countMatches(lastHomeMatchesOfHomeTeam, { it.isDraw() })
    matchStatistics[27] = countMatches(lastHomeMatchesOfHomeTeam, { it.isAwayWin() })

    def lastAwayMatchesOfAwayTeam = Matches.lastAwayMatchesBefore(matchDate, awayTeam)
    matchStatistics[28] = playedMatches(lastAwayMatchesOfAwayTeam)
    matchStatistics[29] = countMatches(lastAwayMatchesOfAwayTeam, { it.isHomeWin() })
    matchStatistics[30] = countMatches(lastAwayMatchesOfAwayTeam, { it.isDraw() })
    matchStatistics[31] = countMatches(lastAwayMatchesOfAwayTeam, { it.isAwayWin() })

    matchStatistics
  }

  private double playedMatches(matches) {
    matches.size() / 100.0
  }

  private double countMatches(matches, typeOfMatch) {
    matches.count(typeOfMatch) / Math.max(1.0, (double) matches.size())
  }
}
