package com.solar.solarsimulator.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AppPropertiesTest {

  private final AppProperties appProperties = new AppProperties();

  @Test
  void whenRunWithArgsIsTrue_thenItShouldReturnTrue() {
    appProperties.setRunWithArgs(true);
    assertThat(appProperties.isRunWithArgs()).isTrue();
  }

  @Test
  void whenRunWithArgsIsFalse_thenItShouldReturnFalse() {
    appProperties.setRunWithArgs(false);
    assertThat(appProperties.isRunWithArgs()).isFalse();
  }
}
