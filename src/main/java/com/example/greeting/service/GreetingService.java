package com.example.greeting.service;

import com.example.greeting.exception.InvalidInputException;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

  /**
   * Builds the greeting message for the given name.
   *
   * <p>Rules (per assignment):
   * <ul>
   *   <li>Missing, empty or blank name -&gt; invalid</li>
   *   <li>First letter A-M / a-m -&gt; valid</li>
   *   <li>First letter N-Z / n-z -&gt; invalid</li>
   *   <li>Non A-Z first character (digit, symbol, unicode) -&gt; invalid</li>
   * </ul>
   *
   * @param name raw query parameter value, may be null
   * @return greeting message, e.g. "Hello Alice"
   * @throws InvalidInputException if the input is invalid
   */
  public String greet(String name) {
    if (name == null) {
      throw new InvalidInputException();
    }
    String trimmed = name.trim();
    if (trimmed.isEmpty()) {
      throw new InvalidInputException();
    }
    char first = trimmed.charAt(0);
    char upper = Character.toUpperCase(first);
    if (upper < 'A' || upper > 'Z') {
      throw new InvalidInputException();
    }
    if (upper >= 'A' && upper <= 'M') {
      return "Hello " + capitalize(trimmed);
    }
    throw new InvalidInputException();
  }

  private static String capitalize(String value) {
    return Character.toUpperCase(value.charAt(0)) + value.substring(1);
  }
}
