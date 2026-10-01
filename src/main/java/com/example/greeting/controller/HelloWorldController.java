package com.example.greeting.controller;

import com.example.greeting.dto.ErrorResponse;
import com.example.greeting.dto.HelloResponse;
import com.example.greeting.exception.InvalidInputException;
import com.example.greeting.service.GreetingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

  private final GreetingService greetingService;

  public HelloWorldController(GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  @GetMapping(value = "/hello-world", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> helloWorld(@RequestParam(value = "name", required = false) String name) {
    try {
      String message = greetingService.greet(name);
      return ResponseEntity.ok(new HelloResponse(message));
    } catch (InvalidInputException ex) {
      return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Input"));
    }
  }
}
