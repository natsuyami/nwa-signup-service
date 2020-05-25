package com.natsuyami.project.nwa.service;

import com.natsuyami.project.nwa.common.constant.NwaContentType;
import com.natsuyami.project.nwa.common.dto.NwaTokenDto;
import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import com.natsuyami.project.nwa.dto.NwaSignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

@Service
public class NwaSignupService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NwaSignupService.class);

  @Autowired
  NwaRestTemplate nwaRestTemplate;

  public NwaTokenDto signup() {
    LOGGER.info("Initialized signup service");

    BodyInserters.FormInserter bodyParam = nwaRestTemplate.createToken("nwa-signup-app", "ed0c6e08-9b3b-4f73-8be4-2505ad72059d", "user", "User!23");

    NwaTokenDto token = nwaRestTemplate.post("http://localhost:8080/auth/realms/NWASpringBoot/protocol/openid-connect/token", bodyParam, NwaContentType.URL_ENCODED, NwaTokenDto.class);

    try {
      if (token != null) {
        NwaSignUpDto user = new NwaSignUpDto();

        user.setEmail("bunquintest@gmail.com");
        user.setFirstName("newSign");
        user.setLastName("usingApi");
        user.setUsername("testingApiUser");
        user.setEnabled(true);

        nwaRestTemplate.post("http://localhost:8080/auth/admin/realms/NWASpringBoot/users", user, NwaContentType.JSON, token.getAccess_token(), String.class);
      }
    } catch (Exception e) {
      LOGGER.info("Error: {}, \n {}", e.getMessage(), e.getStackTrace());
    }

    return token;
  }
}
