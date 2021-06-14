package org.leandror.jaya.exchange_rate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Controller {

  @GetMapping(path = "/", produces = "application/json")
  public String test() {
    return "Hello!";
  }
}
