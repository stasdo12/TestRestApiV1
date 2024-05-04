package com.RestApi.task.TestRestApi.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.RestApi.task.TestRestApi.util.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testValidUser() {
        user.setEmail("example@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");

        assertDoesNotThrow(() -> {
            ValidationUtils.validate(user);
        });
    }

    @Test
    public void testInvalidEmail() {
        user.setEmail("invalid_email");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationUtils.validate(user);
        });
    }


    @Test
    public void testFirstNameIsNotNull() {
        user.setEmail("invalidemail@gmail.com");
        user.setFirstName("");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationUtils.validate(user);
        });
    }

    @Test
    public void testLastNameIsNotNull() {
        user.setEmail("invalidemail@gmail.com");
        user.setFirstName("Doe");
        user.setLastName("");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationUtils.validate(user);
        });
    }

    @Test
    public void testBirthDateIsFuture() {
        user.setEmail("invalidemail@gmail.com");
        user.setFirstName("Doe");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(2029, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationUtils.validate(user);
        });
    }

    @Test
    public void testBirthDateIsNotNull() {
        user.setEmail("invalidemail@gmail.com");
        user.setFirstName("Doe");
        user.setLastName("Doe");
        user.setBirthDate(null);
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationUtils.validate(user);
        });
    }


}
