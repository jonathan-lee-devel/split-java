package io.jonathanlee.emailservice.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import io.jonathanlee.emailservice.config.SecurityConfig;
import io.jonathanlee.emailservice.dto.MailDto;
import io.jonathanlee.emailservice.service.RawMailService;
import io.jonathanlee.emailservice.service.RawMailService.MailSendStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@Import(SecurityConfig.class)
@WebFluxTest(MailController.class)
class MailControllerTest {

  private static final String VALID_EMAIL = "johndoe@mail.com";
  private static final String SUBJECT = "Test Subject";
  private static final String TEXT = "Test text";

  @MockBean
  private RawMailService rawMailService;

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void whenPostMail_andInvalidMailDtoNullAddressTo_thenStatusBadRequestAndReturnMailResponseFailure() {
    final MailDto nullMailDto = MailDto
        .builder()
        .addressTo(null)
        .subject(SUBJECT)
        .text(TEXT)
        .build();

    this.webTestClient
        .mutateWith(csrf())
        .post()
        .uri(MailController.MAIL_CONTROLLER_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(nullMailDto))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody().jsonPath("$.mail_send_status", MailSendStatus.FAILURE.toString());
  }

  @Test
  void whenPostMail_andInvalidMailDtoNullSubject_thenStatusBadRequestAndReturnMailResponseFailure() {
    final MailDto nullMailDto = MailDto
        .builder()
        .addressTo(VALID_EMAIL)
        .subject(null)
        .text(TEXT)
        .build();

    this.webTestClient
        .mutateWith(csrf())
        .post()
        .uri(MailController.MAIL_CONTROLLER_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(nullMailDto))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody().jsonPath("$.mail_send_status", MailSendStatus.FAILURE.toString());
  }

  @Test
  void whenPostMail_andInvalidMailDtoNullText_thenStatusBadRequestAndReturnMailResponseFailure() {
    final MailDto nullMailDto = MailDto
        .builder()
        .addressTo(VALID_EMAIL)
        .subject(SUBJECT)
        .text(null)
        .build();

    this.webTestClient
        .mutateWith(csrf())
        .post()
        .uri(MailController.MAIL_CONTROLLER_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(nullMailDto))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody().jsonPath("$.mail_send_status", MailSendStatus.FAILURE.toString());
  }

  @Test
  void whenPostMail_andValidMailDtoSendSuccess_thenStatusOkAndReturnMailResponseSuccess() {
    final MailDto validMailDto = MailDto
        .builder()
        .addressTo(VALID_EMAIL)
        .subject(SUBJECT)
        .text(TEXT)
        .build();

    Mockito
        .when(this.rawMailService.sendMail(anyString(), anyString(), anyString()))
        .thenReturn(Mono.just(MailSendStatus.SENT));

    this.webTestClient
        .mutateWith(csrf())
        .post()
        .uri(MailController.MAIL_CONTROLLER_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(validMailDto))
        .exchange()
        .expectStatus().isOk()
        .expectBody().jsonPath("$.mail_send_status", MailSendStatus.SENT.toString());
  }

  @Test
  void whenPostMail_andValidMailDtoFailedToSend_thenStatusInternalServerErrorAndReturnMailResponseFailure() {
    final MailDto validMailDto = MailDto
        .builder()
        .addressTo(VALID_EMAIL)
        .subject(SUBJECT)
        .text(TEXT)
        .build();

    Mockito.when(this.rawMailService.sendMail(anyString(), anyString(), anyString()))
        .thenReturn(Mono.just(MailSendStatus.FAILURE));

    this.webTestClient
        .mutateWith(csrf())
        .post()
        .uri(MailController.MAIL_CONTROLLER_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(validMailDto))
        .exchange()
        .expectStatus().is5xxServerError()
        .expectBody().jsonPath("$.mail_send_status", MailSendStatus.FAILURE.toString());
  }

}