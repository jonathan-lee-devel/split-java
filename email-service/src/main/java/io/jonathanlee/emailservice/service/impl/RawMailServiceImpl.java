package io.jonathanlee.emailservice.service.impl;

import io.jonathanlee.emailservice.service.RawMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of raw mail service used which interacts directly with the Java Mail Sender.
 *
 * @author Jonathan Lee <jonathan.lee.devel@gmail.com>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RawMailServiceImpl implements RawMailService {

  private final MailSender mailSender;

  /**
   * Sends email based on parameters provided.
   *
   * @param addressTo email address to which the email is to be sent.
   * @param subject   email subject for the message to be sent.
   * @param text      text or inner content of the email being sent.
   * @return Mono containing a status representing the status of the send operation.
   */
  @Override
  public Mono<MailSendStatus> sendMail(
      final String addressTo, final String subject, final String text) {
    final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(addressTo);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(text);

    MailSendStatus mailSendStatus = MailSendStatus.FAILURE;

    try {
      this.mailSender.send(simpleMailMessage);
      mailSendStatus = MailSendStatus.SENT;
      log.info("E-mail attempted to send with status: {}", mailSendStatus);
    } catch (MailException ex) {
      log.error(ex.getMessage());
    }

    return Mono.just(mailSendStatus);
  }

}
