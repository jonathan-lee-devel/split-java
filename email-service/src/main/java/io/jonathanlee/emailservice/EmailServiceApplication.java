package io.jonathanlee.emailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Main entry point for the application.
 *
 * @author Jonathan Lee <jonathan.lee.devel@gmail.com>
 */
@EnableWebFlux
@SpringBootApplication
public class EmailServiceApplication {

  /**
   * Main entry point for the application.
   *
   * @param args command-line arguments.
   */
  public static void main(final String[] args) {
    SpringApplication.run(EmailServiceApplication.class, args);
  }

}
