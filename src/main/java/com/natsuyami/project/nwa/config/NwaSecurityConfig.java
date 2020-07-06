package com.natsuyami.project.nwa.config;

import com.natsuyami.project.nwa.common.config.NwaCommonSecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class NwaSecurityConfig extends NwaCommonSecurityConfig {

  @Value("${spring.enable.cors}")
  private boolean enableCors;

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    super.configure(http);
    if (enableCors) {
      http
          .csrf()
          .disable()
          .authorizeRequests()
          .anyRequest()
          .hasRole("ADMIN");
    } else {
      http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
  }
}
