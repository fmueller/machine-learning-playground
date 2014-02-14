package de.cupofjava.machinelearning.soccer.worldcup.feature

import org.joda.time.LocalDate

/**
 * @author fmueller
 */
public interface Feature {

  /**
   * Returns the number of input values which
   * are calculated by a certain feature implementation.
   */
  int getSize()

  /**
   * Computes the input values for match between {@code homeTeam}
   * and {@code awayTeam} on {@code matchDate}.
   */
  double[] compute(LocalDate matchDate, String homeTeam, String awayTeam)
}
