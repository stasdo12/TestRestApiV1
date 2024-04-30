package com.RestApi.task.TestRestApi.controllers;

import com.RestApi.task.TestRestApi.entity.User;
import com.RestApi.task.TestRestApi.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserControllerTest {


    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;


    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }


    @Test
    void getAllUsersTest() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "Test@gmail.com", "TestFirstName", "TestLastName",
                LocalDate.now(), "TestAddress", "000000000"));

        when(userService.getAllUsers(PageRequest.of(0, 10))).thenReturn(users);
        mockMvc.perform(get("/api/V1/users/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(userService, times(1)).getAllUsers(PageRequest.of(0, 10));
    }


    @Test
    void createUserTest() throws Exception {
        LocalDate dateOfBirth = LocalDate.of(1994, 4, 26);
        User user = new User(111, "Test@gmail.com", "TestFirstName", "TestLastName",
                dateOfBirth, "TestAddress", "0639539901");
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/V1/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(user);
    }

//    @Test
//    void createUserInvalidBirthDateTest() throws Exception {
//        // Given
//        LocalDate dateOfBirth = LocalDate.of(2021, 2, 12);
//        User user = new User(111, "Test@gmail.com", "TestFirstName", "TestLastName",
//                dateOfBirth, "TestAddress", "0639539901");
//        String userJson = objectMapper.writeValueAsString(user);
//        ErrorResponse expectedErrorResponse = new ErrorResponse("Age must be 18+");
//
//        // When
//        mockMvc.perform(post("/api/users/createUser")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Age must be 18+"));
//
//        // Then
//        // Verify that createUser method is not called
//        verify(userService, never()).createUser(user);
//    }

    @Test
    void deleteUserByIdTest() throws Exception {
        Long userId = 123L;

        mockMvc.perform(delete("/api/V1/users/deleteUser/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void updateUserTest() throws Exception {
        Long userId = 123L;
        User userDetails = new User();
        userDetails.setEmail("updatedEmail@gmail.com");
        userDetails.setFirstName("UpdatedFirstName");
        userDetails.setLastName("UpdatedLastName");

        User updatedUser = new User(userId, "updatedEmail@gmail.com", "UpdatedFirstName", "UpdatedLastName",
                userDetails.getBirthDate(), userDetails.getAddress(), userDetails.getPhoneNumber());

        when(userService.updateUser(userId, userDetails)).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/V1/users/updateUser/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"updatedEmail@gmail.com\", \"firstName\": \"UpdatedFirstName\", \"lastName\": \"UpdatedLastName\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(userId, userDetails);
    }


    @Test
    void updateUserFullTest() throws Exception {
        Long userId = 123L;
        User userDetails = new User();
        userDetails.setEmail("updatedEmail@gmail.com");
        userDetails.setFirstName("UpdatedFirstName");
        userDetails.setLastName("UpdatedLastName");

        User updatedUser = new User(userId, "updatedEmail@gmail.com", "UpdatedFirstName", "UpdatedLastName",
                userDetails.getBirthDate(), userDetails.getAddress(), userDetails.getPhoneNumber());

        when(userService.updateUser(userId, userDetails)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/V1/users/updateUserFull/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"updatedEmail@gmail.com\"," +
                                " \"firstName\": \"UpdatedFirstName\", \"lastName\":" +
                                " \"UpdatedLastName\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(userId, userDetails);
    }

    @Test
    void searchUsersByBirthDateRangeTest() throws Exception {
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "email1@gmail.com", "FirstName1", "LastName1", LocalDate.of(1993, 5, 10), "Address1", "1234567890"));
        users.add(new User(2L, "email2@gmail.com", "FirstName2", "LastName2", LocalDate.of(1994, 8, 20), "Address2", "0987654321"));
        when(userService.getUsersByBirthDateRange(PageRequest.of(0, 10), startDate, endDate)).thenReturn(users);

        mockMvc.perform(get("/api/V1/users/searchUser")
                        .param("from", startDate.toString())
                        .param("to", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));

        verify(userService, times(1)).getUsersByBirthDateRange(PageRequest.of(0, 10), startDate, endDate);
    }

    @Test
    void searchUsersByBirthDateRangeBadRequestTest() throws Exception {
        LocalDate startDate = LocalDate.of(1995, 1, 1);
        LocalDate endDate = LocalDate.of(1990, 12, 31);

        mockMvc.perform(get("/api/V1/users/searchUser")
                        .param("from", startDate.toString())
                        .param("to", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}