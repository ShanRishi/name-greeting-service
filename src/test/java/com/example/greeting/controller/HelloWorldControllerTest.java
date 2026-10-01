package com.example.greeting.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.greeting.service.GreetingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloWorldController.class)
@Import(GreetingService.class)
class HelloWorldControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void validName_returns200WithMessage() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", "alice"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Hello Alice"));
  }

  @Test
  void validNameUppercase_returns200() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", "Bob"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello Bob"));
  }

  @Test
  void boundaryM_returns200() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", "mike"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello Mike"));
  }

  @Test
  void secondHalfName_returns400WithError() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", "zoe"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Input"));
  }

  @Test
  void boundaryN_returns400() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", "nancy"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Input"));
  }

  @Test
  void missingParam_returns400() throws Exception {
    mockMvc.perform(get("/hello-world"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Input"));
  }

  @Test
  void emptyParam_returns400() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", ""))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Input"));
  }

  @Test
  void blankParam_returns400() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", "   "))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Input"));
  }

  @Test
  void digitFirstChar_returns400() throws Exception {
    mockMvc.perform(get("/hello-world").param("name", "123"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Input"));
  }
}
