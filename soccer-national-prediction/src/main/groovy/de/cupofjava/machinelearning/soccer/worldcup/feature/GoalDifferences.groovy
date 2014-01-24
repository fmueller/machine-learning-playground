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
    4
  }

  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] goalDifferences = new double[getSize()]
    goalDifferences[0] = computeGoalDiff(Matches.lastHomeMatchesBefore(matchDate, homeTeam), homeTeam)
    goalDifferences[1] = computeGoalDiff(Matches.lastMatchesBefore(matchDate, homeTeam), homeTeam)
    goalDifferences[2] = computeGoalDiff(Matches.lastAwayMatchesBefore(matchDate, awayTeam), awayTeam)
    goalDifferences[3] = computeGoalDiff(Matches.lastMatchesBefore(matchDate, awayTeam), awayTeam)
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
