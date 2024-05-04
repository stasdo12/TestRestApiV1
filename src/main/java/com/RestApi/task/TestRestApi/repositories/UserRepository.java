package com.RestApi.task.TestRestApi.repositories;

import com.RestApi.task.TestRestApi.entity.User;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Page<User> findByBirthDateBetween(Pageable pageable, LocalDate startDate, LocalDate endDate);

}
