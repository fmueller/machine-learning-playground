package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class GoalAverages implements Feature {

  /**
   * {@inheritDoc}
   */
  @Override
  int getSize() {
    32
  }

  /**
   * {@inheritDoc}
   */
  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] goalAverages = new double[getSize()]

    def allMatchesOfHomeTeam = Matches.allMatchesBefore(matchDate, homeTeam)
    goalAverages[0] = goalAverage(allMatchesOfHomeTeam, homeTeam)
    goalAverages[1] = goalAverage(allMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalAverages[2] = goalAverage(allMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalAverages[3] = goalAverage(allMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def allMatchesOfAwayTeam = Matches.allMatchesBefore(matchDate, awayTeam)
    goalAverages[4] = goalAverage(allMatchesOfAwayTeam, awayTeam)
    goalAverages[5] = goalAverage(allMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalAverages[6] = goalAverage(allMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalAverages[7] = goalAverage(allMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    def lastMatchesOfHomeTeam = Matches.lastMatchesBefore(matchDate, homeTeam)
    goalAverages[8] = goalAverage(lastMatchesOfHomeTeam, homeTeam)
    goalAverages[9] = goalAverage(lastMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalAverages[10] = goalAverage(lastMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalAverages[11] = goalAverage(lastMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def lastMatchesOfAwayTeam = Matches.lastMatchesBefore(matchDate, awayTeam)
    goalAverages[12] = goalAverage(lastMatchesOfAwayTeam, awayTeam)
    goalAverages[13] = goalAverage(lastMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalAverages[14] = goalAverage(lastMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalAverages[15] = goalAverage(lastMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    def allHomeMatchesOfHomeTeam = Matches.allHomeMatchesBefore(matchDate, homeTeam)
    goalAverages[16] = goalAverage(allHomeMatchesOfHomeTeam, homeTeam)
    goalAverages[17] = goalAverage(allHomeMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalAverages[18] = goalAverage(allHomeMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalAverages[19] = goalAverage(allHomeMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def allAwayMatchesOfAwayTeam = Matches.allAwayMatchesBefore(matchDate, awayTeam)
    goalAverages[20] = goalAverage(allAwayMatchesOfAwayTeam, awayTeam)
    goalAverages[21] = goalAverage(allAwayMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalAverages[22] = goalAverage(allAwayMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalAverages[23] = goalAverage(allAwayMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    def lastHomeMatchesOfHomeTeam = Matches.lastHomeMatchesBefore(matchDate, homeTeam)
    goalAverages[24] = goalAverage(lastHomeMatchesOfHomeTeam, homeTeam)
    goalAverages[25] = goalAverage(lastHomeMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalAverages[26] = goalAverage(lastHomeMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalAverages[27] = goalAverage(lastHomeMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def lastAwayMatchesOfAwayTeam = Matches.lastAwayMatchesBefore(matchDate, awayTeam)
    goalAverages[28] = goalAverage(lastAwayMatchesOfAwayTeam, awayTeam)
    goalAverages[29] = goalAverage(lastAwayMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalAverages[30] = goalAverage(lastAwayMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalAverages[31] = goalAverage(lastAwayMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    goalAverages
  }

  private double goalAverage(Collection<Match> matches, String team) {
    int goals = 0
    for (Match match : matches) {
      if (match.isHomeTeam(team)) {
        goals += match.getHomeGoals()
      } else {
        goals += match.getAwayGoals()
      }
    }
    goals / Math.max(1.0, 10.0 * matches.size())
  }
}
