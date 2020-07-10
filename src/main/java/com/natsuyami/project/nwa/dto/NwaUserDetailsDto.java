package com.natsuyami.project.nwa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class NwaUserDetailsDto implements Serializable {

  private static final long serialVersionUID = 6265775581008156147L;

  @ApiModelProperty(hidden = true)
  private long id;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private String username;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private String firstName;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private String lastName;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private String email;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private String passphrase;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private String passcode;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassphrase() {
    return passphrase;
  }

  public void setPassphrase(String passphrase) {
    this.passphrase = passphrase;
  }

  public String getPasscode() {
    return passcode;
  }

  public void setPasscode(String passcode) {
    this.passcode = passcode;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("\"username\" : \"");
    builder.append(username);
    builder.append("\", \"email\" : \"");
    builder.append(email);
    builder.append("\", \"firstName\" : \"");
    builder.append(firstName);
    builder.append("\", \"lastName\" : \"");
    builder.append(lastName);
    builder.append("\", \"passphrase\" : \"");
    builder.append(passphrase);
    builder.append("\", \"passcode\" : \"");
    builder.append(passcode);
    builder.append("\"}");
    return builder.toString();
  }
}
