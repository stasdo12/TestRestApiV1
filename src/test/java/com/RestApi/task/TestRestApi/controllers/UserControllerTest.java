package com.RestApi.task.TestRestApi.controllers;

import com.RestApi.task.TestRestApi.dto.UserDTO;
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
import org.modelmapper.ModelMapper;
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

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserController userController;


    private MockMvc mockMvc;

    private ObjectMapper objectMapper;




    @BeforeEach
    void setUp() {
        userController = new UserController(userService, modelMapper);
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
        mockMvc.perform(get("/api/V1/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(userService, times(1)).getAllUsers(PageRequest.of(0, 10));
    }


    @Test
    void createUserTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("Test@gmail.com");
        userDTO.setFirstName("TestFirstName");
        userDTO.setLastName("TestLastName");
        userDTO.setBirthDate(LocalDate.of(1994, 4, 26));
        userDTO.setAddress("TestAddress");
        userDTO.setPhoneNumber("0639539901");
        String userJson = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/api/V1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
        verify(userService, times(1)).createUser(any(User.class));
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

        mockMvc.perform(delete("/api/V1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void updateUserTest() throws Exception {
        Long userId = 123L;
        UserDTO userDetails = new UserDTO();
        userDetails.setEmail("updatedEmail@gmail.com");
        userDetails.setFirstName("UpdatedFirstName");
        userDetails.setLastName("UpdatedLastName");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setEmail("updatedEmail@gmail.com");
        updatedUserDTO.setFirstName("UpdatedFirstName");
        updatedUserDTO.setLastName("UpdatedLastName");

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(convertToUser(updatedUserDTO));

        mockMvc.perform(patch("/api/V1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"updatedEmail@gmail.com\", \"firstName\": \"UpdatedFirstName\", \"lastName\": \"UpdatedLastName\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
    }


    @Test
    void updateUserFullTest() throws Exception {
        Long userId = 123L;
        UserDTO userDetails = new UserDTO();
        userDetails.setEmail("updatedEmail@gmail.com");
        userDetails.setFirstName("UpdatedFirstName");
        userDetails.setLastName("UpdatedLastName");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setEmail("updatedEmail@gmail.com");
        updatedUserDTO.setFirstName("UpdatedFirstName");
        updatedUserDTO.setLastName("UpdatedLastName");

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(convertToUser(updatedUserDTO));
        mockMvc.perform(put("/api/V1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"updatedEmail@gmail.com\"," +
                                " \"firstName\": \"UpdatedFirstName\", \"lastName\":" +
                                " \"UpdatedLastName\"}"))
                .andExpect(status().isOk());
        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
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

    public User convertToUser(UserDTO userDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDTO, User.class);
    }

}