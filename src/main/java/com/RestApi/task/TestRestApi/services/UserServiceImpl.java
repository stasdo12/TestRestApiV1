package com.RestApi.task.TestRestApi.services;

import com.RestApi.task.TestRestApi.entity.User;
import com.RestApi.task.TestRestApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;



@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;


    @Transactional
    @Override
    public User createUser(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate minBirthDate = currentDate.minusYears(18);

        if (user.getBirthDate().isAfter(minBirthDate)) {
            throw new IllegalArgumentException("Age must be 18+");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId));

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
                .orElseThrow (() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User not found with id: " + userId));

        userRepository.delete(user);

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByBirthDateRange(LocalDate startDate, LocalDate endDate) {
        return userRepository.findByBirthDateBetween(startDate, endDate);

    }


}
