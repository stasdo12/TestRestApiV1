package com.RestApi.task.TestRestApi.services;

import com.RestApi.task.TestRestApi.entity.User;
import com.RestApi.task.TestRestApi.exceptions.AgeRequirementException;
import com.RestApi.task.TestRestApi.exceptions.UserNotFoundException;
import com.RestApi.task.TestRestApi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${user.min-age}")
    private int minAge;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User createUser(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate minBirthDate = currentDate.minusYears(minAge);

        if (user.getBirthDate().isAfter(minBirthDate)) {
            throw new AgeRequirementException("User must be 18 years old or older.");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getBirthDate() != null) {
            user.setBirthDate(userDetails.getBirthDate());
        }
        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }
        if (userDetails.getPhoneNumber() != null) {
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }
        if (userDetails.getFirstName() != null){
            user.setFirstName(userDetails.getFirstName());
        }
        if (userDetails.getLastName() != null){
            user.setLastName(userDetails.getLastName());
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUserFull(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setEmail(userDetails.getEmail());
        user.setBirthDate(userDetails.getBirthDate());
        user.setAddress(userDetails.getAddress());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow (() -> new UserNotFoundException(userId));

        userRepository.delete(user);
    }

    @Override
    public List<User> getAllUsers(PageRequest pageRequest) {
        Page<User> page = userRepository.findAll(pageRequest);
        return page.getContent();
    }

    @Override
    public List<User> getUsersByBirthDateRange(PageRequest pageRequest,  LocalDate startDate, LocalDate endDate) {
        return userRepository.findByBirthDateBetween(pageRequest, startDate, endDate);
    }


}
