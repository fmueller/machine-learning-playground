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
    int homePoints = Matches.allHomeMatchesBefore(matchDate, homeTeam).grep{ it.isHomeWin() }.size() * 3 + Matches.allHomeMatchesBefore(matchDate, homeTeam).grep{ it.isDraw() }.size()
    int awayPoints = Matches.allAwayMatchesBefore(matchDate, homeTeam).grep{ it.isAwayWin() }.size() * 3 + Matches.allAwayMatchesBefore(matchDate, homeTeam).grep{ it.isDraw() }.size()

    double[] hostFactor = new double[getSize()]
    hostFactor[0] = homePoints / (double) (awayPoints > 0 ? awayPoints : 1)
    hostFactor
  }
}
