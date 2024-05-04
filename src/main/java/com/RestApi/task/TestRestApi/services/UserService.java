package com.RestApi.task.TestRestApi.services;

import com.RestApi.task.TestRestApi.dto.UpdatePhoneNumberRq;
import com.RestApi.task.TestRestApi.dto.UserDto;

import java.time.LocalDate;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userDto the user details
     * @return the created user
     */
    UserDto createUser(@Valid UserDto userDto);

    /**
     * Updates the phone number of a user.
     *
     * @param userId      the id of the user
     * @param userDetails the new phone number details
     * @return the updated user
     */
    UserDto updatePhone(Long userId, @Valid UpdatePhoneNumberRq userDetails);

    /**
     * Deletes a user.
     *
     * @param userId the id of the user
     */
    void deleteUser(Long userId);

    /**
     * Updates the full details of a user.
     *
     * @param userId      the id of the user
     * @param userDetails the new user details
     * @return the updated user
     */
    UserDto updateUserFull(Long userId, UserDto userDetails);

    /**
     * Retrieves all users with pagination.
     *
     * @param pageable the pagination details
     * @return the page of users
     */
    Page<UserDto> getAllUsers(Pageable pageable);

    /**
     * Retrieves users by birthdate range with pagination.
     *
     * @param pageable  the pagination details
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return the page of users
     */
    Page<UserDto> getUsersByBirthDateRange(Pageable pageable, LocalDate startDate,
                                           LocalDate endDate);
}