package com.RestApi.task.TestRestApi.services;

import com.RestApi.task.TestRestApi.dto.UpdatePhoneNumberRq;
import com.RestApi.task.TestRestApi.dto.UserDto;
import com.RestApi.task.TestRestApi.entity.User;
import com.RestApi.task.TestRestApi.exceptions.UserNotFoundException;
import com.RestApi.task.TestRestApi.mapper.UserMapper;
import com.RestApi.task.TestRestApi.repositories.UserRepository;
import com.RestApi.task.TestRestApi.validator.UserDataValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDataValidator userDataValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john@example.com");
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        userDto.setBirthDate(birthDate);

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setBirthDate(birthDate);

        when(userMapper.convertToUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.convertToDTO(user)).thenReturn(userDto);

        UserDto result = userService.createUser(userDto);

        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getLastName(), result.getLastName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getBirthDate(), result.getBirthDate());
    }


    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(createUser("John", "Doe", "john@example.com"));
        userList.add(createUser("Jane", "Smith", "jane@example.com"));

        Pageable pageable = Pageable.unpaged();
        Page<User> userPage = new PageImpl<>(userList);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        List<UserDto> expectedUserDtoList = new ArrayList<>();
        for (User user : userList) {
            expectedUserDtoList.add(userMapper.convertToDTO(user));
        }

        Page<UserDto> result = userService.getAllUsers(pageable);

        assertEquals(expectedUserDtoList.size(), result.getContent().size());
    }


    @Test
    void testGetUsersByBirthDateRange() {
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1991, 1, 1);
        Pageable pageable = Pageable.unpaged();

        List<User> userList = new ArrayList<>();
        userList.add(createUser("John", "Doe", "john@example.com",
                LocalDate.of(1990, 6, 15)));
        userList.add(createUser("Jane", "Smith", "jane@example.com",
                LocalDate.of(1990, 7, 20)));

        Page<User> userPage = new PageImpl<>(userList);

        when(userRepository.findByBirthDateBetween(pageable, startDate, endDate)).thenReturn(userPage);
        when(userMapper.convertToDTO(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new UserDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getBirthDate(), user.getAddress(), user.getPhoneNumber());
        });

        List<UserDto> expectedUserDtoList = new ArrayList<>();
        for (User user : userList) {
            expectedUserDtoList.add(userMapper.convertToDTO(user));
        }

        Page<UserDto> result = userService.getUsersByBirthDateRange(pageable, startDate, endDate);

        assertEquals(expectedUserDtoList.size(), result.getContent().size());

        verify(userDataValidator).validateDateRange(startDate, endDate);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void testUpdateUserFull() {
        Long userId = 1L;
        UserDto userDetails = new UserDto();
        userDetails.setEmail("new.email@example.com");
        userDetails.setBirthDate(LocalDate.of(1990, 10, 26));
        userDetails.setAddress("456 New St");
        userDetails.setPhoneNumber("987654321");
        userDetails.setFirstName("NewFirstName");
        userDetails.setLastName("NewLastName");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old.email@example.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail(userDetails.getEmail());
        updatedUser.setBirthDate(userDetails.getBirthDate());
        updatedUser.setAddress(userDetails.getAddress());
        updatedUser.setPhoneNumber(userDetails.getPhoneNumber());
        updatedUser.setFirstName(userDetails.getFirstName());
        updatedUser.setLastName(userDetails.getLastName());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMapper.convertToDTO(updatedUser)).thenReturn(userDetails);

        UserDto result = userService.updateUserFull(userId, userDetails);

        assertEquals(userDetails, result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(updatedUser);
        verify(userMapper).convertToDTO(updatedUser);
    }

    @Test
    void testUpdatePhone() {
        Long userId = 1L;
        String newPhoneNumber = "987654321";
        LocalDate date = LocalDate.of(1990, 1, 1);

        UpdatePhoneNumberRq userDetails = new UpdatePhoneNumberRq();
        userDetails.setPhoneNumber(newPhoneNumber);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("TestFirstName");
        existingUser.setLastName("TestFirstName");
        existingUser.setBirthDate(date);
        existingUser.setEmail("TestFirstName@gmaik.com");
        existingUser.setPhoneNumber("123456789");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updatePhone(userId, userDetails);

        verify(userRepository).save(existingUser); // Verify that the save method was called
    }


    private User createUser(String firstName, String lastName, String email) {
        User user = new User();
        user.setId(1L);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }

    private User createUser(String firstName, String lastName, String email, LocalDate birthDate) {
        User user = new User();
        user.setId(1L);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        return user;
    }
}
