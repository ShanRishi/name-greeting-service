package com.example.greeting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.greeting.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GreetingServiceTest {

  private GreetingService greetingService;

  @BeforeEach
  void setUp() {
    greetingService = new GreetingService();
  }

  @ParameterizedTest
  @ValueSource(strings = {"alice", "Alice", "bob", "Mike", "m", "M", "A", "a", "Charlie"})
  void validNames_firstHalfAlphabet_returnsGreeting(String name) {
    String expected = "Hello " + Character.toUpperCase(name.trim().charAt(0)) + name.trim().substring(1);
    assertThat(greetingService.greet(name)).isEqualTo(expected);
  }

  @Test
  void validName_lowercase_capitalizesFirstLetter() {
    assertThat(greetingService.greet("alice")).isEqualTo("Hello Alice");
  }

  @Test
  void validName_withSurroundingSpaces_trimsThenGreets() {
    assertThat(greetingService.greet("  alice  ")).isEqualTo("Hello Alice");
  }

  @ParameterizedTest
  @ValueSource(strings = {"nancy", "Nancy", "zoe", "Zoe", "N", "n", "Z", "z"})
  void invalidNames_secondHalfAlphabet_throws(String name) {
    assertThatThrownBy(() -> greetingService.greet(name))
        .isInstanceOf(InvalidInputException.class);
  }

  @Test
  void nullName_throws() {
    assertThatThrownBy(() -> greetingService.greet(null))
        .isInstanceOf(InvalidInputException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void emptyOrBlankName_throws(String name) {
    assertThatThrownBy(() -> greetingService.greet(name))
        .isInstanceOf(InvalidInputException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {"123", "1alice", "!bob", "-alice", "1lice", "élice", "über"})
  void nonLetterFirstChar_throws(String name) {
    assertThatThrownBy(() -> greetingService.greet(name))
        .isInstanceOf(InvalidInputException.class);
  }

  @Test
  void boundaryM_valid_N_invalid() {
    assertThat(greetingService.greet("mike")).isEqualTo("Hello Mike");
    assertThatThrownBy(() -> greetingService.greet("nancy"))
        .isInstanceOf(InvalidInputException.class);
  }
}
