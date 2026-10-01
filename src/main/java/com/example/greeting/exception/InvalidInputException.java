package com.example.greeting.exception;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException() {
    super("Invalid Input");
  }

  public InvalidInputException(String message) {
    super(message);
  }
}
