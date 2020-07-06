package com.natsuyami.project.nwa.service;

import com.natsuyami.project.nwa.common.constant.NwaContentType;
import com.natsuyami.project.nwa.common.dto.NwaTokenDto;
import com.natsuyami.project.nwa.common.encrypt.NwaContentEncyption;
import com.natsuyami.project.nwa.common.encrypt.NwaPasswordEncrypt;
import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import com.natsuyami.project.nwa.common.dto.NwaCredentialDto;
import com.natsuyami.project.nwa.dto.NwaUserDetailsDto;
import com.natsuyami.project.nwa.common.dto.NwaUserKeycloakDto;
import com.natsuyami.project.nwa.common.dto.NwaUserRoleDto;
import com.natsuyami.project.nwa.model.NwaUserModel;
import com.natsuyami.project.nwa.repository.NwaUserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@Service
public class NwaSignupService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NwaSignupService.class);
  private static final String GRANT_TYPE = "password";

  @Autowired
  private NwaRestTemplate nwaRestTemplate;

  @Autowired
  private NwaUserRepository nwaUserRepository;

  @Value("${keycloak.resource}")
  private String clientId;

  @Value("${keycloak.credentials.secret}")
  private String clientSecret;

  @Value("${kc.account.manager.username}")
  private String accountUsername;

  @Value("${kc.account.manager.password}")
  private String accountPassword;

  @Value("${encryption.private.key}")
  private String privateKey;

  public String signup(NwaUserDetailsDto signupDto) throws Exception {
    LOGGER.info("Initialized signup service, signup details signupDto={{}}", signupDto);

    BodyInserters.FormInserter bodyParam = nwaRestTemplate.createToken(clientId, clientSecret, accountUsername, accountPassword);
    NwaTokenDto token = nwaRestTemplate.post("http://localhost:8080/auth/realms/NWASpringBoot/protocol/openid-connect/token", bodyParam, NwaContentType.URL_ENCODED, null, NwaTokenDto.class);

    try {
      if (token != null) {
        NwaUserDetailsDto validateSignupDto = validateUserDetails(signupDto);
        validateSignupDto.setPassword(NwaPasswordEncrypt.encrypt(signupDto.getPassword(), String.valueOf(signupDto.getPasscode())));
        NwaUserModel save = saveUser(validateSignupDto);

        if (save != null) {
          String[] hashVal = NwaPasswordEncrypt.originalEncryption(signupDto.getPassword(), String.valueOf(signupDto.getPasscode()));
          save.setPassword(hashVal[0].concat(".").concat(hashVal[1]));
          saveInKeycloak(save, token);
        } else {
          throw new Exception("Failed to create user details");
        }
      }
    } catch (Exception e) {
      LOGGER.info("Error creating user {}, \n {}", e.getMessage(), e.getStackTrace());
      throw e;
    }

    return "Success Signup";
  }

  public NwaUserDetailsDto validateUserDetails(NwaUserDetailsDto signupDto) throws Exception {
    if (signupDto != null) {
      Pattern specialChar = Pattern.compile("[$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]");
      Pattern specialCharNumber = Pattern.compile("[0-9$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]");
      Pattern specialCharNumLet = Pattern.compile("[a-zA-Z0-9$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]");

      String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
          "[a-zA-Z0-9_+&*-]+)*@" +
          "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
          "A-Z]{2,7}$";

      Pattern emailValidate = Pattern.compile(emailRegex);
      signupDto.setUsername(NwaContentEncyption.decrypt(signupDto.getUsername(), privateKey));
      signupDto.setEmail(NwaContentEncyption.decrypt(signupDto.getEmail(), privateKey));
      signupDto.setPassword(NwaContentEncyption.decrypt(signupDto.getPassword(), privateKey));
      signupDto.setConfirmPassword(NwaContentEncyption.decrypt(signupDto.getConfirmPassword(), privateKey));
      signupDto.setPasscode(NwaContentEncyption.decrypt(signupDto.getPasscode(), privateKey));

      //decrypt email first before validate
      if (StringUtils.isNotBlank(signupDto.getEmail())) {
        if (signupDto.getEmail().length() <= 4 || emailValidate.matcher(signupDto.getEmail()).matches() == false) {
          throw new Exception("Invalid email format");
        }
      } else {
        throw new Exception("Email is required");
      }

      if (StringUtils.isNotBlank(signupDto.getFirstName()) && StringUtils.isNotBlank(signupDto.getLastName())) {
        if (specialCharNumber.matcher(signupDto.getFirstName()).find() || specialCharNumber.matcher(signupDto.getLastName()).find()) {
          throw new Exception("First and last name should only have letters");
        }
      } else {
        throw new Exception("First and last name are required");
      }

      //decrypt username first before validate
      if (StringUtils.isNotBlank(signupDto.getUsername())) {
        if (signupDto.getUsername().length() <= 4 || specialChar.matcher(signupDto.getUsername()).find()) {
          throw new Exception("Username must not have special character and has a minimum length of 5");
        }
      } else {
        throw new Exception("Username is required");
      }

      //decrypt password first before validate
      if (StringUtils.isNotBlank(signupDto.getPassword()) && StringUtils.isNotBlank(signupDto.getConfirmPassword())) {
        if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
          throw new Exception("Password does not match");
        } else if (signupDto.getPassword().length() < 5 || specialCharNumLet.matcher(signupDto.getPassword()).find() == false) {
          throw new Exception("Password must have a special character, number, upper and lower letter");
        } else {}
      } else {
        throw new Exception("Password is required");
      }

      if (signupDto.getPasscode() == null || signupDto.getPasscode() < 99999) {
        throw new Exception("Passcode is required and has 6 digit");
      }

      return signupDto;
    } else {
      throw new Exception("Sign-up details such as username, email, password, etc. are required");
    }
  }

  private NwaUserModel saveUser(NwaUserDetailsDto signupDto) {
    NwaUserModel userModel = new NwaUserModel();
    userModel.setUsername(signupDto.getUsername());
    userModel.setEmail(signupDto.getEmail());
    userModel.setFirstName(signupDto.getFirstName());
    userModel.setLastName(signupDto.getLastName());
    userModel.setPassword(signupDto.getPassword());

    return nwaUserRepository.save(userModel);
  }

  private void saveInKeycloak(NwaUserModel user, NwaTokenDto token) throws Exception {
    LOGGER.info("Saving user details in keycloak, creating username={{}}", user.getUsername());

    NwaUserKeycloakDto keycloakData = new NwaUserKeycloakDto();
    keycloakData.setUsername(user.getUsername());
    keycloakData.setEmail(user.getEmail());
    keycloakData.setEnabled(true);
    keycloakData.setFirstName(user.getFirstName());
    keycloakData.setLastName(user.getLastName());

    NwaCredentialDto credential = new NwaCredentialDto();
    credential.setType(GRANT_TYPE);
    credential.setValue(user.getPassword());
    credential.setTemporary(false);

    ArrayList<NwaCredentialDto> credentials = new ArrayList<NwaCredentialDto>();
    credentials.add(credential);

    keycloakData.setCredentials(credentials);

    Map<String, ArrayList> attribs = new HashMap<>();
    ArrayList<String> customerId = new ArrayList<>();
    customerId.add(String.valueOf(user.getId()));
    attribs.put("customerId", customerId);

    keycloakData.setAttributes(attribs);
    nwaRestTemplate.post("http://localhost:8080/auth/admin/realms/NWASpringBoot/users", keycloakData, NwaContentType.JSON, token.getAccessToken(), String.class);

    MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
    requestParam.add("username", keycloakData.getUsername());
    String uri = nwaRestTemplate.uriBuilder("http://localhost:8080/auth/admin/realms/NWASpringBoot/users", requestParam);

    LOGGER.info("Getting the data of created user, username={{}}", keycloakData.getUsername());
    List<NwaUserKeycloakDto> userKeycloakDetails = nwaRestTemplate.getList(uri, NwaContentType.JSON, token.getAccessToken(), NwaUserKeycloakDto.class);

    if (userKeycloakDetails != null) {
      LOGGER.info("Adding realm role to the user, username={{}}", userKeycloakDetails.get(0).getUsername());
      NwaUserRoleDto roleOne = new NwaUserRoleDto();
      roleOne.setId("1e3a1439-c58d-47a0-b419-374802e87633");
      roleOne.setName("nwa-user");
      ArrayList<NwaUserRoleDto> roles = new ArrayList<>();
      roles.add(roleOne);
      nwaRestTemplate.post("http://localhost:8080/auth/admin/realms/NWASpringBoot/users/"  + userKeycloakDetails.get(0).getId() + "/role-mappings/realm", roles, NwaContentType.JSON, token.getAccessToken(), String.class);
    } else {
      LOGGER.error("Error getting User details, might cause of unsuccessful create. keycloakData={}", keycloakData);
      throw new Exception("Failed to create user credentials");
    }
  }
}
