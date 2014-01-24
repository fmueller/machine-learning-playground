package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class GoalAverages implements Feature {

  @Override
  int getSize() {
    8
  }

  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] goalAverages = new double[getSize()]
    goalAverages[0] = goalAverage(Matches.allHomeMatchesBefore(matchDate, homeTeam).grep{ it.isHomeWin() }, homeTeam)
    goalAverages[1] = goalAverage(Matches.allAwayMatchesBefore(matchDate, awayTeam).grep{ it.isAwayWin() }, awayTeam)
    goalAverages[2] = goalAverage(Matches.allHomeMatchesBefore(matchDate, homeTeam).grep{ it.isAwayWin() }, homeTeam)
    goalAverages[3] = goalAverage(Matches.allAwayMatchesBefore(matchDate, awayTeam).grep{ it.isHomeWin() }, awayTeam)
    goalAverages[4] = goalAverage(Matches.lastHomeMatchesBefore(matchDate, homeTeam).grep{ it.isHomeWin() }, homeTeam)
    goalAverages[5] = goalAverage(Matches.lastAwayMatchesBefore(matchDate, awayTeam).grep{ it.isAwayWin() }, awayTeam)
    goalAverages[6] = goalAverage(Matches.lastHomeMatchesBefore(matchDate, homeTeam).grep{ it.isAwayWin() }, homeTeam)
    goalAverages[7] = goalAverage(Matches.lastAwayMatchesBefore(matchDate, awayTeam).grep{ it.isHomeWin() }, awayTeam)
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
    goals / (10.0 * (matches.size() > 0 ? matches.size() : 1))
  }
}
