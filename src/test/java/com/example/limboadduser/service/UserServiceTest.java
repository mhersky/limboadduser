package com.example.limboadduser.service;

import com.example.limboadduser.model.User;
import com.example.limboadduser.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;

    private transient User user;
    private static String TEST_USERNAME = "Test User";
    private static String TEST_EMAIL = "email.me@test.com";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name(TEST_USERNAME)
                .email(TEST_EMAIL)
                .build();
    }

    @Test
    void testRegisterUser() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));


        User createdUser = userService.registerUser(user);

        assertNotNull(createdUser);
        assertEquals(TEST_USERNAME, createdUser.getName());
        assertEquals(TEST_EMAIL, createdUser.getEmail());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getUserById(1L);

        assertNotNull(retrievedUser);
        assertEquals(TEST_USERNAME, retrievedUser.getName());
        assertEquals(TEST_EMAIL, retrievedUser.getEmail());
    }

    @Test
    void testUpdateUser() {
        User updatedUser = User.builder()
                .id(1L)
                .name(TEST_USERNAME)
                .email("test@test.com")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getName());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void testSoftDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.softDeleteUser(1L);

        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSoftMultipleDeleteUsers() {
        User user2 = User.builder()
                .id(2L)
                .name(TEST_USERNAME + "2")
                .email("email2.me@test.com")
                .build();
        List<Long> ids = Arrays.asList(1L, 2L);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user))
                .thenReturn(Optional.of(user2));

        userService.softDeleteMultipleUsers(ids);

        assertFalse(user.isActive());
        assertFalse(user2.isActive());
        verify(userRepository, times(2)).save(any());
    }

}