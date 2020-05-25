package com.natsuyami.project.nwa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class NwaSignUpDto implements Serializable {

  private static final long serialVersionUID = 6265775581008156147L;

  @ApiModelProperty(hidden = true)
  private long id;

  @JsonInclude(Include.NON_NULL)
  private String username;

  @JsonInclude(Include.NON_NULL)
  private String firstName;

  @JsonInclude(Include.NON_NULL)
  private String lastName;

  @JsonInclude(Include.NON_NULL)
  private String email;

  @JsonInclude(Include.NON_NULL)
  private String password;

  @JsonInclude(Include.NON_NULL)
  private String code;

  private Boolean enabled;

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
