package de.cupofjava.machinelearning.soccer.worldcup

import org.junit.Test

import static org.fest.assertions.Assertions.assertThat

class MatchPredictionTest {

  @Test
  void homeWinPredicted() {
    MatchPrediction prediction = new MatchPrediction(null, 0.11, 0.1, 0.09)
    assertThat(prediction.isHomeWinPredicted()).isTrue()
    assertThat(prediction.isDrawPredicted()).isFalse()
    assertThat(prediction.isAwayWinPredicted()).isFalse()
  }

  @Test
  void drawPredicted() {
    MatchPrediction prediction = new MatchPrediction(null, 0.2, 0.21, 0.19)
    assertThat(prediction.isHomeWinPredicted()).isFalse()
    assertThat(prediction.isDrawPredicted()).isTrue()
    assertThat(prediction.isAwayWinPredicted()).isFalse()
  }

  @Test
  void awayWinPredicted() {
    MatchPrediction prediction = new MatchPrediction(null, 0.39, 0.4, 0.401)
    assertThat(prediction.isHomeWinPredicted()).isFalse()
    assertThat(prediction.isDrawPredicted()).isFalse()
    assertThat(prediction.isAwayWinPredicted()).isTrue()
  }

  @Test
  void homeWinWhenProbabilityOfDrawIsEqual() {
    MatchPrediction prediction = new MatchPrediction(null, 0.1, 0.1, 0.0)
    assertThat(prediction.isHomeWinPredicted()).isTrue()
    assertThat(prediction.isDrawPredicted()).isFalse()
    assertThat(prediction.isAwayWinPredicted()).isFalse()
  }

  @Test
  void homeWinWhenProbabilityOfAwayWinIsEqual() {
    MatchPrediction prediction = new MatchPrediction(null, 0.2, 0.0, 0.2)
    assertThat(prediction.isHomeWinPredicted()).isTrue()
    assertThat(prediction.isDrawPredicted()).isFalse()
    assertThat(prediction.isAwayWinPredicted()).isFalse()
  }

  @Test
  void awayWinWhenProbabilityOfDrawIsEqual() {
    MatchPrediction prediction = new MatchPrediction(null, 0.0, 0.2, 0.2)
    assertThat(prediction.isHomeWinPredicted()).isFalse()
    assertThat(prediction.isDrawPredicted()).isFalse()
    assertThat(prediction.isAwayWinPredicted()).isTrue()
  }
}
