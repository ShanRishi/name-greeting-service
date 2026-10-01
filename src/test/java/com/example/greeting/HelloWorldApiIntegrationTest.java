package com.example.greeting;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.greeting.dto.ErrorResponse;
import com.example.greeting.dto.HelloResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloWorldApiIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void validName_returns200() {
    ResponseEntity<HelloResponse> response =
        restTemplate.getForEntity("/hello-world?name=alice", HelloResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().message()).isEqualTo("Hello Alice");
  }

  @Test
  void invalidName_returns400() {
    ResponseEntity<ErrorResponse> response =
        restTemplate.getForEntity("/hello-world?name=zoe", ErrorResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().error()).isEqualTo("Invalid Input");
  }

  @Test
  void missingParam_returns400() {
    ResponseEntity<ErrorResponse> response =
        restTemplate.getForEntity("/hello-world", ErrorResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().error()).isEqualTo("Invalid Input");
  }
}
