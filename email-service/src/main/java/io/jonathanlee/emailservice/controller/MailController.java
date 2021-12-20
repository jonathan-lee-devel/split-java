package io.jonathanlee.emailservice.controller;

import io.jonathanlee.emailservice.dto.MailDto;
import io.jonathanlee.emailservice.dto.MailResponseDto;
import io.jonathanlee.emailservice.service.RawMailService;
import io.jonathanlee.emailservice.service.RawMailService.MailSendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * REST controller used to send e-mails.
 *
 * @author Jonathan Lee <jonathan.lee.devel@gmail.com>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(MailController.MAIL_CONTROLLER_BASE_URI)
public class MailController {

  public static final String MAIL_CONTROLLER_BASE_URI = "/mail";

  private final RawMailService rawMailService;

  /**
   * Endpoint used to send e-mails.
   *
   * @param mailDto mail data transfer object containing data for sending email.
   * @return status representing whether the email was successfully sent.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<MailResponseDto>> postMail(@RequestBody final MailDto mailDto) {
    if (mailDto.getAddressTo() == null
        || mailDto.getSubject() == null
        || mailDto.getText() == null) {
      return Mono.just(
          ResponseEntity.badRequest().body(new MailResponseDto(MailSendStatus.FAILURE)));
    }

    Mono<MailResponseDto> mailResponseDto =
        this.rawMailService.sendMail(
                mailDto.getAddressTo(), mailDto.getSubject(), mailDto.getText())
            .map(MailResponseDto::new);

    return mailResponseDto.map(generatedMailResponseDto -> {
      if (generatedMailResponseDto.getMailSendStatus() == MailSendStatus.SENT) {
        return ResponseEntity.ok(generatedMailResponseDto);
      } else {
        return ResponseEntity.internalServerError().body(generatedMailResponseDto);
      }
    });
  }

}
