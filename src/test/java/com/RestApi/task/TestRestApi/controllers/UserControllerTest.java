package com.RestApi.task.TestRestApi.controllers;

import com.RestApi.task.TestRestApi.dto.UpdatePhoneNumberRq;
import com.RestApi.task.TestRestApi.dto.UserDto;
import com.RestApi.task.TestRestApi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    void createUserTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/V1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userService, times(1)).createUser(userDto);
    }

    @Test
    void getAllUsersTest() {
        List<UserDto> users = new ArrayList<>();
        users.add(new UserDto("test1@gmail.com", "John", "Doe",
                LocalDate.of(1990, 1, 1), "Address1", "1234567890"));
        users.add(new UserDto("test2@gmail.com", "Jane", "Smith",
                LocalDate.of(1995, 5, 5), "Address2", "0987654321"));

        Page<UserDto> userPage = new PageImpl<>(users);

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userPage);

        ResponseEntity<Page<UserDto>> responseEntity = userController.getAllUsers(Pageable.unpaged());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(users.size(), responseEntity.getBody().getNumberOfElements());
        assertEquals(users.get(0), responseEntity.getBody().getContent().get(0));
        assertEquals(users.get(1), responseEntity.getBody().getContent().get(1));

        verify(userService, times(1)).getAllUsers(any(Pageable.class));
    }

    @Test
    void deleteUserTest() {
        Long userId = 1L;
        ResponseEntity<Void> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void updateContactsTest() throws Exception {
        Long userId = 1L;
        UpdatePhoneNumberRq updatePhoneRq = new UpdatePhoneNumberRq();
        updatePhoneRq.setPhoneNumber("1234567890");

        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("test@example.com");
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");
        updatedUser.setPhoneNumber("1234567890");

        when(userService.updatePhone(userId, updatePhoneRq)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/V1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePhoneRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));

        verify(userService, times(1)).updatePhone(userId, updatePhoneRq);
    }

    @Test
    void updateUserFullTest() throws Exception {
        Long userId = 1L;
        UserDto userDTO = new UserDto();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("test@example.com");
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");

        when(userService.updateUserFull(userId, userDTO)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/V1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userService, times(1)).updateUserFull(userId, userDTO);
    }

    @Test
    void searchUsersByBirthDateRangeTest() throws Exception {
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 1, 1);
        List<UserDto> users = new ArrayList<>();
        UserDto user1 = new UserDto();
        user1.setEmail("user1@example.com");
        user1.setFirstName("Alice");
        user1.setLastName("Smith");
        user1.setBirthDate(LocalDate.of(1992, 3, 15));
        users.add(user1);

        UserDto user2 = new UserDto();
        user2.setEmail("user2@example.com");
        user2.setFirstName("Bob");
        user2.setLastName("Johnson");
        user2.setBirthDate(LocalDate.of(1990, 8, 22));
        users.add(user2);

        Page<UserDto> userPage = new PageImpl<>(users);

        when(userService.getUsersByBirthDateRange(any(Pageable.class), eq(startDate), eq(endDate))).thenReturn(userPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/V1/users/searchUser")
                        .param("from", startDate.toString())
                        .param("to", endDate.toString()))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUsersByBirthDateRange(any(Pageable.class), eq(startDate), eq(endDate));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
