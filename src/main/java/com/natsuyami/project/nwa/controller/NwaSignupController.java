package com.natsuyami.project.nwa.controller;

import com.natsuyami.project.nwa.service.NwaSignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/signup", produces = "application/json")
@RestController
public class NwaSignupController {

  private static final Logger LOGGER = LoggerFactory.getLogger(NwaSignupController.class);

  @Autowired
  NwaSignupService nwaSignupService;

  @GetMapping
  public Object signup() {
    LOGGER.info("Initialized signup controller");

    return nwaSignupService.signup();
  }
}
