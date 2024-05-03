package com.RestApi.task.TestRestApi.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.RestApi.task.TestRestApi.entity.User;
import com.RestApi.task.TestRestApi.exceptions.UserNotFoundException;
import com.RestApi.task.TestRestApi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

//    @Test
//    public void testFindByBirthDateBetween() {
//        List<User> users = createUsers();
//
//        LocalDate startDate = LocalDate.of(1990, 1, 1);
//        LocalDate endDate = LocalDate.of(1990, 2, 28);
//        when(userRepository.findByBirthDateBetween(PageRequest.of(0, 10), startDate, endDate)).thenReturn(users);
//
//        List<User> result = userService.getUsersByBirthDateRange(PageRequest.of(0, 10),  startDate, endDate);
//
//        assertEquals(2, result.size());
//    }

    @Test
    public void testGetUsersByBirthDateRange1() {
        // Создаем тестовых пользователей
        List<User> users = createUsers();

        // Устанавливаем даты начала и конца для поиска пользователей
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1990, 2, 28);

        // Устанавливаем ожидаемый результат при вызове метода findByBirthDateBetween
        when(userRepository.findByBirthDateBetween(any(Pageable.class), eq(startDate), eq(endDate)))
                .thenReturn(new PageImpl<>(users));

        // Вызываем тестируемый метод
        Page<User> result = userService.getUsersByBirthDateRange1(PageRequest.of(0, 10), startDate, endDate);

        // Проверяем корректность результатов
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testCreateUser() {
        User userToCreate = new User();
        userToCreate.setId(1L);
        userToCreate.setEmail("example@example.com");
        userToCreate.setFirstName("John");
        userToCreate.setLastName("Doe");
        userToCreate.setBirthDate(LocalDate.of(1990, 1, 1));

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("example@example.com");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");

        when(userRepository.save(userToCreate)).thenReturn(savedUser);

        User createdUser = userService.createUser(userToCreate);

        assertNotNull(createdUser);
    }


    @Test
    public void testGetAllUsers() {
        int page = 0;
        int size = 10;
        List<User> users = createUsers();
        Page<User> pageUsers = new PageImpl<>(users);

        when(userRepository.findAll(PageRequest.of(page, size))).thenReturn(pageUsers);

        List<User> allUsers = userService.getAllUsers(PageRequest.of(page, size));
        assertEquals(2, allUsers.size());

        assertEquals(1L, allUsers.get(0).getId());
        assertEquals("example1@example.com", allUsers.get(0).getEmail());
        assertEquals("John", allUsers.get(0).getFirstName());
        assertEquals("Doe", allUsers.get(0).getLastName());

        assertEquals(2L, allUsers.get(1).getId());
        assertEquals("example1@example.com", allUsers.get(1).getEmail());
        assertEquals("Jane", allUsers.get(1).getFirstName());
        assertEquals("Doe", allUsers.get(1).getLastName());
    }

    @Test
    public void testDeleteUser_Success() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        userService.deleteUser(userId);
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }


    private List<User> createUsers() {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("example1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(1990, 1, 1));
        user1.setAddress("123 Main St");
        user1.setPhoneNumber("1234567890");
        users.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("example1@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setBirthDate(LocalDate.of(1990, 1, 1));
        user2.setAddress("123 Main St");
        user2.setPhoneNumber("1234567890");
        users.add(user2);

        return users;
    }
}
