package io.jonathanlee.emailservice.service;

import reactor.core.publisher.Mono;

/**
 * Raw mail service used which interacts directly with the Java Mail Sender.
 *
 * @author Jonathan Lee <jonathan.lee.devel@gmail.com>
 */
public interface RawMailService {

  /**
   * Sends email based on parameters provided.
   *
   * @param addressTo email address to which the email is to be sent.
   * @param subject   email subject for the message to be sent.
   * @param text      text or inner content of the email being sent.
   * @return Mono containing a status representing the status of the send operation.
   */
  Mono<MailSendStatus> sendMail(
      final String addressTo, final String subject, final String text);

  enum MailSendStatus {
    FAILURE,
    SENT
  }

}
