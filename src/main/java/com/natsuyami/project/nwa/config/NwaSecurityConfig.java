package com.natsuyami.project.nwa.config;

import com.natsuyami.project.nwa.common.config.NwaCommonSecurityConfig;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class NwaSecurityConfig extends NwaCommonSecurityConfig {

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    super.configure(http);
    http
        .authorizeRequests().anyRequest().permitAll();
  }
}
