package de.cupofjava.machinelearning.soccer.worldcup.feature

import de.cupofjava.machinelearning.soccer.worldcup.Matches
import org.joda.time.LocalDate

/**
 * @author fmueller
 */
final class HostFactor implements Feature {

  @Override
  int getSize() {
    1
  }

  @Override
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam) {
    double allHomeMatches = Matches.allHomeMatchesBefore(matchDate, homeTeam).size()
    double allAwayMatches = Matches.allAwayMatchesBefore(matchDate, homeTeam).size()

    double homePointsRatio = (Matches.allHomeMatchesBefore(matchDate, homeTeam).grep { it.isHomeWin() }.size() * 3
        + Matches.allHomeMatchesBefore(matchDate, homeTeam).grep { it.isDraw() }.size()) / allHomeMatches > 0 ? allHomeMatches : 1.0
    double awayPointsRatio = (Matches.allAwayMatchesBefore(matchDate, homeTeam).grep { it.isAwayWin() }.size() * 3
        + Matches.allAwayMatchesBefore(matchDate, homeTeam).grep { it.isDraw() }.size()) / allAwayMatches > 0 ? allAwayMatches : 1.0

    double[] hostFactor = new double[getSize()]
    hostFactor[0] = homePointsRatio - awayPointsRatio
    hostFactor
  }
}
