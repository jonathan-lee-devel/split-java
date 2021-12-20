package io.jonathanlee.emailservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Mail data transfer object used when sending emails.
 *
 * @author Jonathan Lee <jonathan.lee.devel@gmail.com>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MailDto {

  @JsonProperty("address_to")
  private String addressTo;

  private String subject;

  private String text;

}
