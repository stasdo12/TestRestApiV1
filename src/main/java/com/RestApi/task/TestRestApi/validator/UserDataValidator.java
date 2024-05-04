package com.RestApi.task.TestRestApi.validator;

import com.RestApi.task.TestRestApi.exceptions.AgeRequirementException;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDataValidator {

  public void validateUserAge(LocalDate birthDate, int minAge) {
    LocalDate currentDate = LocalDate.now();
    LocalDate minBirthDate = currentDate.minusYears(minAge);

    if (birthDate.isAfter(minBirthDate)) {
      log.error("User must be 18 years old or older.");
      throw new AgeRequirementException("User must be 18 years old or older.");
    }
  }

  public void validateDateRange(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      log.error("Start date must be before end date.");
      throw new IllegalArgumentException("Start date must be before end date.");
    }
  }


}
