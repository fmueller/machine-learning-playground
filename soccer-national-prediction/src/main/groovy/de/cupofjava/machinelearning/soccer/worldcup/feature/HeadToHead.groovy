package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Match
import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class HeadToHead implements Feature {

  /**
   * {@inheritDoc}
   */
  @Override
  int getSize() {
    26
  }

  /**
   * {@inheritDoc}
   */
  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double[] headToHeadComparison = new double[getSize()]

    def allMatches = Matches.allMatchesBefore(matchDate, homeTeam, awayTeam)
    def allHomeMatches = Matches.allHomeMatchesBefore(matchDate, homeTeam, awayTeam)
    def lastMatches = Matches.lastMatchesBefore(matchDate, homeTeam, awayTeam)
    def lastHomeMatches = Matches.lastHomeMatchesBefore(matchDate, homeTeam, awayTeam)

    headToHeadComparison[0] = computeGoalDiff(allMatches, homeTeam)
    headToHeadComparison[1] = goalAverage(allMatches, homeTeam)
    headToHeadComparison[2] = goalAverage(allMatches, awayTeam)

    headToHeadComparison[3] = computeGoalDiff(allHomeMatches, homeTeam)
    headToHeadComparison[4] = goalAverage(allHomeMatches, homeTeam)
    headToHeadComparison[5] = goalAverage(allHomeMatches, awayTeam)

    headToHeadComparison[6] = computeGoalDiff(lastMatches, homeTeam)
    headToHeadComparison[7] = goalAverage(lastMatches, homeTeam)
    headToHeadComparison[8] = goalAverage(lastMatches, awayTeam)

    headToHeadComparison[9] = computeGoalDiff(lastHomeMatches, homeTeam)
    headToHeadComparison[10] = goalAverage(lastHomeMatches, homeTeam)
    headToHeadComparison[11] = goalAverage(lastHomeMatches, awayTeam)

    headToHeadComparison[12] = playedMatches(allMatches)
    headToHeadComparison[13] = countMatches(allMatches, { it.isHomeWin() })
    headToHeadComparison[14] = countMatches(allMatches, { it.isDraw() })
    headToHeadComparison[15] = countMatches(allMatches, { it.isAwayWin() })

    headToHeadComparison[16] = playedMatches(allHomeMatches)
    headToHeadComparison[17] = countMatches(allHomeMatches, { it.isHomeWin() })
    headToHeadComparison[18] = countMatches(allHomeMatches, { it.isDraw() })
    headToHeadComparison[19] = countMatches(allHomeMatches, { it.isAwayWin() })

    headToHeadComparison[20] = countMatches(lastMatches, { it.isHomeWin() })
    headToHeadComparison[21] = countMatches(lastMatches, { it.isDraw() })
    headToHeadComparison[22] = countMatches(lastMatches, { it.isAwayWin() })

    headToHeadComparison[23] = countMatches(lastHomeMatches, { it.isHomeWin() })
    headToHeadComparison[24] = countMatches(lastHomeMatches, { it.isDraw() })
    headToHeadComparison[25] = countMatches(lastHomeMatches, { it.isAwayWin() })

    headToHeadComparison
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

  private double playedMatches(matches) {
    matches.size() / 100.0
  }

  private double countMatches(matches, typeOfMatch) {
    matches.count(typeOfMatch) / Math.max(1.0, (double) matches.size())
  }
}
