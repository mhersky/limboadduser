package com.example.limboadduser.controller;

import com.example.limboadduser.model.User;
import com.example.limboadduser.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    private MockMvc mockMvc;
    private transient User user;
    private static String TEST_USERNAME = "Test User";
    private static String TEST_EMAIL = "email.me@test.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userRestController)
                .build();

        user = User.builder()
                .id(1L)
                .name(TEST_USERNAME)
                .email(TEST_EMAIL)
                .build();
    }

    @Test
    void testRegisterUser() throws Exception {
        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api//users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"Test User\", \"email\":\"email.me@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    void testGetUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api//users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api//users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(TEST_USERNAME))
                .andExpect(jsonPath("$[0].email").value(TEST_EMAIL));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api//users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"Test User\", \"email\":\"test@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    void testSoftDeleteUser() throws Exception {
        doNothing().when(userService).softDeleteUser(1L);

        mockMvc.perform(delete("/api//users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSoftMultipleDeleteUsers() throws Exception {
        doNothing().when(userService).softDeleteMultipleUsers(anyList());

        mockMvc.perform(delete("/api//users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("[1, 2, 3]"))
                .andExpect(status().isNoContent());
    }
}