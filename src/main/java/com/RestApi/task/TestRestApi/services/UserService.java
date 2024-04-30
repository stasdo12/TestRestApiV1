package com.RestApi.task.TestRestApi.services;

import com.RestApi.task.TestRestApi.entity.User;
import org.springframework.data.domain.PageRequest;


import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Long userId, User userDetails);
    void deleteUser(Long userId);
    User updateUserFull(Long userId, User userDetails);
    List<User> getAllUsers(PageRequest pageRequest);
    List<User> getUsersByBirthDateRange(PageRequest pageRequest, LocalDate startDate, LocalDate endDate);

}
