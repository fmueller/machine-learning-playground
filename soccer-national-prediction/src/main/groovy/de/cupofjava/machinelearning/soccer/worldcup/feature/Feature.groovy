package de.cupofjava.machinelearning.soccer.worldcup.feature

import org.joda.time.LocalDate

/**
 * @author fmueller
 */
public interface Feature {

  int getSize()

  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam)
}
