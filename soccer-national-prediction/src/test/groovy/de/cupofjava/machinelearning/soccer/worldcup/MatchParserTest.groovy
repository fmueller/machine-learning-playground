package de.cupofjava.machinelearning.soccer.worldcup

import org.joda.time.LocalDate
import org.junit.Test

import static org.fest.assertions.Assertions.assertThat

class MatchParserTest {

  @Test
  void parseOneMatch() {
    Collection<Match> matches = MatchParser.parseMatches("Cameroon;Sudan;3;0;;1.10;7.73;17.11;9;Africa Cup of Nations 2008;;2008-01-30 18:00")
    assertThat(matches).containsOnly(new Match(new LocalDate(2008, 1, 30), "Cameroon", "Sudan", 3, 0))
  }

  @Test
  void parseManyMatches() {
    Collection<Match> matches = MatchParser.parseMatches("Iceland;Cyprus;1;0;;2.06;3.34;3.60;10;Euro 2012;Qualification;2011-09-06 20:45\n" +
        "Italy;Slovenia;1;0;;1.40;4.37;8.59;10;Euro 2012;Qualification;2011-09-06 20:45\n" +
        "Austria;Turkey;0;0;;2.90;3.33;2.41;10;Euro 2012;Qualification;2011-09-06 20:30")
    assertThat(matches).containsOnly(new Match(new LocalDate(2011, 9, 6), "Iceland", "Cyprus", 1, 0),
        new Match(new LocalDate(2011, 9, 6), "Italy", "Slovenia", 1, 0),
        new Match(new LocalDate(2011, 9, 6), "Austria", "Turkey", 0, 0))
  }
}
