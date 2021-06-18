package org.leandror.jaya.exchange_rate.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;

@ExtendWith({ SpringExtension.class, RandomBeansExtension.class })
@WebMvcTest(controllers = ConversionController.class)
class ConversionControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() throws Exception {
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void returnBadRequest_when_desiredCurrencyInvalid(@Random ConversionRequest payload) throws Exception {
    mockMvc.perform(post("/api/v1/conversion").contentType("application/json")
                                                                .content(objectMapper.writeValueAsString(payload)))
                             .andExpect(status().isBadRequest())
                             .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

}
