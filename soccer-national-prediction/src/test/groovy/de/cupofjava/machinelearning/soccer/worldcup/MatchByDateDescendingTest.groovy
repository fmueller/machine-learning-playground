package de.cupofjava.machinelearning.soccer.worldcup

import org.joda.time.LocalDate
import org.junit.Test

import static org.fest.assertions.Assertions.assertThat

class MatchByDateDescendingTest {

  @Test
  void shouldSortDescendingByMatchDate() {
    def matches = Arrays.asList(
        new Match(new LocalDate(1900, 1, 1), "", "", 0, 0, 0.0, 0.0, 0.0),
        new Match(new LocalDate(1899, 12, 31), "", "", 0, 0, 0.0, 0.0, 0.0),
        new Match(new LocalDate(1900, 1, 2), "", "", 0, 0, 0.0, 0.0, 0.0)
    )
    Collections.sort(matches, new MatchByDateDescending())
    assertThat(matches.get(0).getDate().getDayOfMonth()).isEqualTo(2)
    assertThat(matches.get(1).getDate().getDayOfMonth()).isEqualTo(1)
    assertThat(matches.get(2).getDate().getDayOfMonth()).isEqualTo(31)
  }
}
