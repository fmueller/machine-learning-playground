package de.cupofjava.machinelearning.soccer.worldcup

import au.com.bytecode.opencsv.CSVReader
import com.google.common.collect.Sets
import org.joda.time.format.DateTimeFormat

/**
 * @author fmueller
 */
class MatchParser {

  private static def DATE_PARSER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")

  static Collection<Match> parseMatches(String csvContent) {
    def matches = Sets.newHashSet()
    def reader = new CSVReader(new StringReader(csvContent.trim()), (char) ';')
    List<String[]> lines = reader.readAll()
    for (String[] line : lines) {
      matches.add(new Match(DATE_PARSER.parseLocalDate(line[11]),
          line[0],
          line[1],
          Integer.parseInt(line[2]),
          Integer.parseInt(line[3])))
    }
    matches
  }
}
