package com.RestApi.task.TestRestApi.controllers;

import com.RestApi.task.TestRestApi.entity.User;
import com.RestApi.task.TestRestApi.exceptions.ErrorResponse;
import com.RestApi.task.TestRestApi.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@AutoConfigureMockMvc
@SpringBootTest

class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "john.doe@example.com", "John", "Doe",
                        LocalDate.of(1990, 5, 15), "123 Main Street", "+1234567890"),
                new User(2L, "alice.smith@example.com", "Alice", "Smith",
                        LocalDate.of(1985, 10, 20), "456 Oak Avenue", "+1987654321")

        );
        when(userService.getAllUsers()).thenReturn(users);
        ResponseEntity<List<User>> response = userController.getAllUsers();
        verify(userService, times(1)).getAllUsers();
        assertEquals(users, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateUser_ValidBirthDate() {
        User user = new User(1L, "john.doe@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main Street", "+1234567890");

        LocalDate currentDate = LocalDate.of(2024, 4, 29); // Пример текущей даты
        int minAge = 18;
        LocalDate minBirthDate = currentDate.minusYears(minAge);
        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<Object> response = userController.createUser(user);
        verify(userService, times(1)).createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testCreateUser_InvalidBirthDate() {
        User user = new User(1L, "john.doe@example.com", "John", "Doe",
                LocalDate.of(2040, 4, 29), "123 Main Street", "+1234567890");

        ResponseEntity<Object> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertEquals("Age must be 18+", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);
        ResponseEntity<?> response = userController.deleteUser(userId);
        verify(userService).deleteUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateUser() {
    }

    @Test
    void updateUserFull() {
    }

    @Test
    void testSearchUsersByBirthDateRange() throws Exception {
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 12, 31);

        mockMvc.perform(MockMvcRequestBuilders.get("/searchUser")
                        .param("from", startDate.toString())
                        .param("to", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}