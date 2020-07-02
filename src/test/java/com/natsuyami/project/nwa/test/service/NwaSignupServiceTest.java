package com.natsuyami.project.nwa.test.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import com.natsuyami.project.nwa.dto.NwaUserDetailsDto;
import com.natsuyami.project.nwa.repository.NwaUserRepository;
import com.natsuyami.project.nwa.service.NwaSignupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class NwaSignupServiceTest {

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
  public void init() {
    signupDto.setUsername("$#*$(#$");
    signupDto.setEmail("dasdasdasd");
    signupDto.setFirstName(null);
    signupDto.setLastName(null);
    signupDto.setCode(12314);
    signupDto.setPassword("Password");
    signupDto.setConfirmPassword("Password");
  }

  @Test
  public void testSignupValidation() throws Exception {

    try {
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Invalid email format", equalTo(e.getMessage()));
    }

    try {
      signupDto.setEmail("bunquinryan@gmail.com");
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("First and last name are required", equalTo(e.getMessage()));
    }

    try {
      signupDto.setFirstName("Ryan@#@#@");
      signupDto.setLastName("Bam#@#@");
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("First and last name should only have letters", equalTo(e.getMessage()));
    }

    try {
      signupDto.setFirstName("Ryan");
      signupDto.setLastName("Bam");
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Username must not have special character and has a minimum length of 5", equalTo(e.getMessage()));
    }

    try {
      signupDto.setUsername("Test1234");
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Passcode is required and has 6 digit", equalTo(e.getMessage()));
    }

    try {
      signupDto.setCode(123456);
      nwaSignupService.validateUserDetails(signupDto);
    } catch (Exception e) {
      assertThat("Password must have a special character, number, upper and lower letter", equalTo(e.getMessage()));
    }

    signupDto.setPassword("Password!234");
    signupDto.setConfirmPassword("Password!234");
    nwaSignupService.validateUserDetails(signupDto);
  }
}
