package io.jonathanlee.emailservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jonathanlee.emailservice.service.RawMailService.MailSendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Mail response data transfer object used when sending emails to represent status of send
 * operation.
 *
 * @author Jonathan Lee <jonathan.lee.devel@gmail.com>
 */
@AllArgsConstructor
@Getter
public class MailResponseDto {

  @JsonProperty("mail_send_status")
  private MailSendStatus mailSendStatus;

}
