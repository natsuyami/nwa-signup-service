package com.natsuyami.project.nwa.controller;

import com.natsuyami.project.nwa.dto.NwaUserDetailsDto;
import com.natsuyami.project.nwa.service.NwaSignupService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/signup", produces = "application/json")
@RestController
public class NwaSignupController {

  private static final Logger LOGGER = LoggerFactory.getLogger(NwaSignupController.class);

  @Autowired
  NwaSignupService nwaSignupService;

  @PostMapping
  @ApiOperation(value = "Signup user account", response = String.class)
  public Object signup(@RequestBody NwaUserDetailsDto signupDto) throws Exception {
    LOGGER.info("Initialized signup controller signupDto={{}}", signupDto);

    return nwaSignupService.signup(signupDto);
  }
}
