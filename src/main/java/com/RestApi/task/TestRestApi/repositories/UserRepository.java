package com.RestApi.task.TestRestApi.repositories;

import com.RestApi.task.TestRestApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface UserRepository extends JpaRepository<User, Long> {

//    List<User> findByBirthDateBetween(PageRequest pageRequest, LocalDate startDate, LocalDate endDate);

    Page<User> findByBirthDateBetween(Pageable pageable, LocalDate startDate, LocalDate endDate);

}
