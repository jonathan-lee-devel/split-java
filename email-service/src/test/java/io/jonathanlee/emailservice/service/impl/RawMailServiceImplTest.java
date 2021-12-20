package io.jonathanlee.emailservice.service.impl;

import static org.mockito.ArgumentMatchers.any;

import io.jonathanlee.emailservice.service.RawMailService.MailSendStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import reactor.core.publisher.Mono;

class RawMailServiceImplTest {

  private static final String VALID_EMAIL = "johndoe@mail.com";
  private static final String SUBJECT = "Test Subject";
  private static final String TEXT = "Test text";

  private RawMailServiceImpl rawMailService;

  private MailSender mailSender;

  @BeforeEach
  void beforeEach() {
    this.mailSender = Mockito.mock(MailSender.class);
    this.rawMailService = new RawMailServiceImpl(this.mailSender);
  }

  @Test
  void whenSendMail_validParameters_thenSendStatusSent() {
    Mono<MailSendStatus> mailSendStatusMono
        = this.rawMailService.sendMail(VALID_EMAIL, SUBJECT, TEXT);

    Assertions.assertEquals(MailSendStatus.SENT, mailSendStatusMono.block());
  }

  @Test
  void whenSendMail_validParameters_thenMailSenderSendSimpleMailMessage() {
    final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(VALID_EMAIL);
    simpleMailMessage.setSubject(SUBJECT);
    simpleMailMessage.setText(TEXT);

    this.rawMailService.sendMail(VALID_EMAIL, SUBJECT, TEXT);

    Mockito
        .verify(this.mailSender, Mockito.atLeastOnce())
        .send(simpleMailMessage);
  }

  @Test
  void whenSendMail_mailSenderThrowsException_thenSendStatusFailure() {
    Mockito
        .doThrow(MailSendException.class)
        .when(this.mailSender).send(any(SimpleMailMessage.class));

    Mono<MailSendStatus> mailSendStatusMono
        = this.rawMailService.sendMail(VALID_EMAIL, SUBJECT, TEXT);

    Assertions.assertEquals(MailSendStatus.FAILURE, mailSendStatusMono.block());
  }

}