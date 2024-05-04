package com.RestApi.task.TestRestApi.controllers;

import com.RestApi.task.TestRestApi.dto.UpdatePhoneNumberRq;
import com.RestApi.task.TestRestApi.dto.UserDto;
import com.RestApi.task.TestRestApi.services.UserService;

import java.time.LocalDate;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/V1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDTO) {
        UserDto response = userService.createUser(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<UserDto> userPage = userService.getAllUsers(pageable);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateContacts(@PathVariable Long userId,
                                                  @Valid @RequestBody UpdatePhoneNumberRq updatePhoneRq) {
        UserDto updatedUser = userService.updatePhone(userId, updatePhoneRq);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserFull(@PathVariable Long userId,
                                                  @Valid @RequestBody UserDto userDTO) {
        UserDto updatedUser = userService.updateUserFull(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/searchUser")
    public ResponseEntity<Page<UserDto>> searchUsersByBirthDateRange1(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        Page<UserDto> userPage = userService.getUsersByBirthDateRange(pageable, startDate, endDate);
        return ResponseEntity.ok(userPage);
    }

}