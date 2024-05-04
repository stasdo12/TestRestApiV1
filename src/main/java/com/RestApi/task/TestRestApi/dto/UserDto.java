package com.RestApi.task.TestRestApi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "First name must not be empty")
    @Size(max = 50, min = 2, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotNull(message = "BirthDate must not be empty")
    private LocalDate birthDate;


    private String address;


    private String phoneNumber;
}