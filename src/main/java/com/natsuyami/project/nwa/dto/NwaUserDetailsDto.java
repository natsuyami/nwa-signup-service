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
  private String password;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private String confirmPassword;

  @NotNull
  @JsonInclude(Include.NON_NULL)
  private Integer code;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
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
    builder.append("\", \"password\" : \"");
    builder.append(password);
    builder.append("\", \"confirmPassword\" : \"");
    builder.append(confirmPassword);
    builder.append("\", \"code\" : \"");
    builder.append(code);
    builder.append("\"}");
    return builder.toString();
  }
}
