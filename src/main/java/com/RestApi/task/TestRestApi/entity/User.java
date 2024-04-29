package com.RestApi.task.TestRestApi.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
@Valid
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Valid
    @Column(name = "email")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Valid
    @Column(name = "firstname")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Valid
    @Column(name = "lastname")
    private String lastName;


    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Valid
    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Valid
    @Column(name = "address")
    private String address;

    @Valid
    @Column(name = "phonenumber")
    private String phoneNumber;


}
