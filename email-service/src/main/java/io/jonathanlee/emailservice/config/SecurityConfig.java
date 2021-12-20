package io.jonathanlee.emailservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Basic security configuration.
 *
 * @author Jonathan Lee <jonathan.lee.devel@gmail.com>
 */
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

  /**
   * Basic security web filter chain configuration.
   *
   * @param http ServerHttpSecurity used to configure security.
   * @return security web filter chain configuration.
   */
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
    return http
        .authorizeExchange()
        .anyExchange().permitAll()
        .and()
        .build();
  }

}
