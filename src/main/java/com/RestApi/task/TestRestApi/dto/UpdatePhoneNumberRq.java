package com.RestApi.task.TestRestApi.dto;

import javax.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UpdatePhoneNumberRq {

  @Pattern(regexp = "\\d{10}", message = "Phone number must contain 10 digits")
  private String phoneNumber;

}
