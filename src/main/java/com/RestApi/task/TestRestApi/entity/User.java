package com.RestApi.task.TestRestApi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name= "user")
@Valid
public class User implements Serializable {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Valid
    @Column(name="email")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Valid
    @Column(name="firstName")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Valid
    @Column(name="lastName")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    @Valid
    @Column(name="birthDate")
    private LocalDate birthDate;

    @Valid
    @Column(name="address")
    private String address;

    @Valid
    @Column(name="phoneNumber")
    private String phoneNumber;









}
