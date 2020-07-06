package com.natsuyami.project.nwa.test.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.natsuyami.project.nwa.common.encrypt.NwaContentEncyption;
import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import com.natsuyami.project.nwa.dto.NwaUserDetailsDto;
import com.natsuyami.project.nwa.repository.NwaUserRepository;
import com.natsuyami.project.nwa.service.NwaSignupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application.properties")
public class NwaSignupServiceTest {

  private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCS7LVbnX94Q+mjOjU2duWxFUJIJkefY548qCLFDXlILcvmhaCTlMeFctGAfKwsRVGlozQpT5DwY9rDLnio0LUtamm3DBo7D/a+kdLKQMwD1jsMcwJIg6kjFI7PE/NWl6+q1/6DsrPuOlzoOB1wFaz6pPZyUUGQAmHAE3zFsefvaQIDAQAB";

  @Autowired
  private NwaSignupService nwaSignupService;

  @MockBean
  private NwaRestTemplate nwaRestTemplate;

  @MockBean
  private NwaUserRepository nwaUserRepository;

  private NwaUserDetailsDto signupDto = new NwaUserDetailsDto();

  @TestConfiguration
  static class NwaSignupServiceTestContextConfiguration {
    @Bean
    public NwaSignupService nwaSignupService() {
      return new NwaSignupService();
    }
  }

  @Before
  public void init() throws Exception {
    signupDto.setUsername(NwaContentEncyption.encrypt("$#*$(#$", publicKey));
    signupDto.setEmail(NwaContentEncyption.encrypt("dasdasdasd", publicKey));
    signupDto.setFirstName(null);
    signupDto.setLastName(null);
    signupDto.setPasscode(NwaContentEncyption.encrypt("12314", publicKey));
    signupDto.setPassword(NwaContentEncyption.encrypt("Password", publicKey));
    signupDto.setConfirmPassword(NwaContentEncyption.encrypt("Password", publicKey));
  }

  @Test
  public void testSignupValidation() throws Exception {
    try {
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Invalid email format", equalTo(e.getMessage()));
    }

    signupDto.setEmail(NwaContentEncyption.encrypt("bunquinryan@gmail.com", publicKey));
    try {
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("First and last name are required", equalTo(e.getMessage()));
    }

    signupDto.setEmail(NwaContentEncyption.encrypt("bunquinryan@gmail.com", publicKey));
    signupDto.setFirstName("Ryan@#@#@");
    signupDto.setLastName("Bam#@#@");
    try {
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("First and last name should only have letters", equalTo(e.getMessage()));
    }

    signupDto.setEmail(NwaContentEncyption.encrypt("bunquinryan@gmail.com", publicKey));
    signupDto.setFirstName("Ryan");
    signupDto.setLastName("Bam");
    try {
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Username must not have special character and has a minimum length of 5", equalTo(e.getMessage()));
    }

    signupDto.setEmail(NwaContentEncyption.encrypt("bunquinryan@gmail.com", publicKey));
    signupDto.setUsername(NwaContentEncyption.encrypt("Test1234", publicKey));
    try {
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Passcode is required and has 6 digit", equalTo(e.getMessage()));
    }

    signupDto.setEmail(NwaContentEncyption.encrypt("bunquinryan@gmail.com", publicKey));
    signupDto.setUsername(NwaContentEncyption.encrypt("Test1234", publicKey));
    signupDto.setPasscode(NwaContentEncyption.encrypt("123456", publicKey));
    signupDto.setPassword(NwaContentEncyption.encrypt("Password", publicKey));
    signupDto.setConfirmPassword(NwaContentEncyption.encrypt("Password", publicKey));
    try {
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Password must have a special character, number, upper and lower letter", equalTo(e.getMessage()));
    }

    signupDto.setEmail(NwaContentEncyption.encrypt("bunquinryan@gmail.com", publicKey));
    signupDto.setUsername(NwaContentEncyption.encrypt("Test1234", publicKey));
    signupDto.setPasscode(NwaContentEncyption.encrypt("123456", publicKey));
    signupDto.setPassword(NwaContentEncyption.encrypt("Password!234", publicKey));
    signupDto.setConfirmPassword(NwaContentEncyption.encrypt("Password!234", publicKey));
    nwaSignupService.validateUserDetails(signupDto);
  }
}
