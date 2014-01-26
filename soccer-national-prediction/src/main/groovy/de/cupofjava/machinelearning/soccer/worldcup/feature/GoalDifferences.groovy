package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class GoalDifferences implements Feature {

  @Override
  int getSize() {
    32
  }

  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] goalDifferences = new double[getSize()]

    def allMatchesOfHomeTeam = Matches.allMatchesBefore(matchDate, homeTeam)
    goalDifferences[0] = computeGoalDiff(allMatchesOfHomeTeam, homeTeam)
    goalDifferences[1] = computeGoalDiff(allMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalDifferences[2] = computeGoalDiff(allMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalDifferences[3] = computeGoalDiff(allMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def allMatchesOfAwayTeam = Matches.allMatchesBefore(matchDate, awayTeam)
    goalDifferences[4] = computeGoalDiff(allMatchesOfAwayTeam, awayTeam)
    goalDifferences[5] = computeGoalDiff(allMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalDifferences[6] = computeGoalDiff(allMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalDifferences[7] = computeGoalDiff(allMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    def lastMatchesOfHomeTeam = Matches.lastMatchesBefore(matchDate, homeTeam)
    goalDifferences[8] = computeGoalDiff(lastMatchesOfHomeTeam, homeTeam)
    goalDifferences[9] = computeGoalDiff(lastMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalDifferences[10] = computeGoalDiff(lastMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalDifferences[11] = computeGoalDiff(lastMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def lastMatchesOfAwayTeam = Matches.lastMatchesBefore(matchDate, awayTeam)
    goalDifferences[12] = computeGoalDiff(lastMatchesOfAwayTeam, awayTeam)
    goalDifferences[13] = computeGoalDiff(lastMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalDifferences[14] = computeGoalDiff(lastMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalDifferences[15] = computeGoalDiff(lastMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    def allHomeMatchesOfHomeTeam = Matches.allHomeMatchesBefore(matchDate, homeTeam)
    goalDifferences[16] = computeGoalDiff(allHomeMatchesOfHomeTeam, homeTeam)
    goalDifferences[17] = computeGoalDiff(allHomeMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalDifferences[18] = computeGoalDiff(allHomeMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalDifferences[19] = computeGoalDiff(allHomeMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def allAwayMatchesOfAwayTeam = Matches.allAwayMatchesBefore(matchDate, awayTeam)
    goalDifferences[20] = computeGoalDiff(allAwayMatchesOfAwayTeam, awayTeam)
    goalDifferences[21] = computeGoalDiff(allAwayMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalDifferences[22] = computeGoalDiff(allAwayMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalDifferences[23] = computeGoalDiff(allAwayMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    def lastHomeMatchesOfHomeTeam = Matches.lastHomeMatchesBefore(matchDate, homeTeam)
    goalDifferences[24] = computeGoalDiff(lastHomeMatchesOfHomeTeam, homeTeam)
    goalDifferences[25] = computeGoalDiff(lastHomeMatchesOfHomeTeam.grep{ it.isHomeWin() }, homeTeam)
    goalDifferences[26] = computeGoalDiff(lastHomeMatchesOfHomeTeam.grep{ it.isDraw() }, homeTeam)
    goalDifferences[27] = computeGoalDiff(lastHomeMatchesOfHomeTeam.grep{ it.isAwayWin() }, homeTeam)

    def lastAwayMatchesOfAwayTeam = Matches.lastAwayMatchesBefore(matchDate, awayTeam)
    goalDifferences[28] = computeGoalDiff(lastAwayMatchesOfAwayTeam, awayTeam)
    goalDifferences[29] = computeGoalDiff(lastAwayMatchesOfAwayTeam.grep{ it.isHomeWin() }, awayTeam)
    goalDifferences[30] = computeGoalDiff(lastAwayMatchesOfAwayTeam.grep{ it.isDraw() }, awayTeam)
    goalDifferences[31] = computeGoalDiff(lastAwayMatchesOfAwayTeam.grep{ it.isAwayWin() }, awayTeam)

    goalDifferences
  }

  private double computeGoalDiff(Collection<Match> matches, String team) {
    double goalDifference = 0
    for (Match match : matches) {
      if (match.isHomeTeam(team)) {
        goalDifference += match.getHomeGoals() - match.getAwayGoals()
      } else {
        goalDifference += match.getAwayGoals() - match.getHomeGoals()
      }
    }
    goalDifference / Math.max(1.0, 10.0 * matches.size())
  }
}
