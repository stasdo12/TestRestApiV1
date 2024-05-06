package com.RestApi.task.TestRestApi.services;

import com.RestApi.task.TestRestApi.dto.UpdatePhoneNumberRq;
import com.RestApi.task.TestRestApi.dto.UserDto;
import com.RestApi.task.TestRestApi.entity.User;
import com.RestApi.task.TestRestApi.exceptions.UserNotFoundException;
import com.RestApi.task.TestRestApi.mapper.UserMapper;
import com.RestApi.task.TestRestApi.repositories.UserRepository;
import com.RestApi.task.TestRestApi.validator.UserDataValidator;

import java.time.LocalDate;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${user.min-age}")
    private int minAge;

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserDataValidator userDataValidator;

    @Transactional
    @Override
    public UserDto createUser(@Valid UserDto userDto) {
        log.info("Creating user: {}", userDto);
        User user = userMapper.convertToUser(userDto);
        userDataValidator.validateUserAge(user.getBirthDate(), minAge);
        return userMapper.convertToDTO(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto updatePhone(Long userId, @Valid UpdatePhoneNumberRq userDetails) {
        log.info("Updating phone number for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setPhoneNumber(userDetails.getPhoneNumber());

        User savedUser = userRepository.save(user);
        return userMapper.convertToDTO(savedUser);
    }

    @Transactional
    @Override
    public UserDto updateUserFull(Long userId, UserDto userDetails) {
        log.info("Updating user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setEmail(userDetails.getEmail());
        user.setBirthDate(userDetails.getBirthDate());
        user.setAddress(userDetails.getAddress());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());

        User save = userRepository.save(user);
        return userMapper.convertToDTO(save);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("Deleting user with id: {}", userId);
        userRepository.delete(user);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Getting all users");
        Page<User> all = userRepository.findAll(pageable);
        return all.map(userMapper::convertToDTO);
    }

    @Override
    public Page<UserDto> getUsersByBirthDateRange(Pageable pageable, LocalDate startDate,
                                                  LocalDate endDate) {
        log.info("Getting users by birth date range");
        userDataValidator.validateDateRange(startDate, endDate);
        Page<User> byBirthDateBetween = userRepository.findByBirthDateBetween(pageable, startDate,
                endDate);
        return byBirthDateBetween.map(userMapper::convertToDTO);
    }
}
