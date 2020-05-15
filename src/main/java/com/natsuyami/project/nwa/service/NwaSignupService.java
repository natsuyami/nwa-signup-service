package com.natsuyami.project.nwa.service;

import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NwaSignupService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NwaSignupService.class);

  @Autowired
  NwaRestTemplate nwaRestTemplate;

  public String signup() {
    LOGGER.info("Initialized signup service");
    
    return "Testing";
  }
}
