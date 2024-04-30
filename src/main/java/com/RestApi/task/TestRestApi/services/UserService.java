package com.RestApi.task.TestRestApi.services;

import com.RestApi.task.TestRestApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Long userId, User userDetails);
    void deleteUser(Long userId);
    User updateUserFull(Long userId, User userDetails);
    List<User> getAllUsers();
    List<User> getUsersByBirthDateRange(LocalDate startDate, LocalDate endDate);

}
