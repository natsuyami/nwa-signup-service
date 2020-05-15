package com.natsuyami.project.nwa.utils;

import com.natsuyami.project.nwa.model.NwaUser;
import java.util.Collections;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public class NwaKeycloakUser {

  public static UserRepresentation getUserRepresentation(NwaUser newUser) {

    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(newUser.getUsername());
    user.setFirstName(newUser.getFirstName());
    user.setLastName(newUser.getLastName());
    user.setEmail(newUser.getEmail());
    
    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setTemporary(true);
    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
    credentialRepresentation.setValue(newUser.getUsername());
    user.setCredentials(Collections.singletonList(credentialRepresentation));

    return user;
  }
}
