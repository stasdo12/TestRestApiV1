package com.RestApi.task.TestRestApi.repositories;

import com.RestApi.task.TestRestApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByBirthDateBetween(PageRequest pageRequest, LocalDate startDate, LocalDate endDate);

}
