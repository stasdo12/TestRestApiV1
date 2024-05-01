package com.RestApi.task.TestRestApi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
public class UserDTO {

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "First name must not be empty")
    @Size(max = 50, min = 2, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    private LocalDate birthDate;

    @NotBlank(message = "Address must not be empty")
    private String address;

    @Pattern(regexp = "\\d{10}", message = "Phone number must contain 10 digits")
    private String phoneNumber;
}