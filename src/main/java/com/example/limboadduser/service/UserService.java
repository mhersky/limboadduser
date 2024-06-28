package com.example.limboadduser.service;

import com.example.limboadduser.exception.UserAlreadyExistsException;
import com.example.limboadduser.exception.UserNotFoundException;
import com.example.limboadduser.model.User;
import com.example.limboadduser.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public User registerUser(@Valid User user) throws MessagingException {
        if (userRepository.findByEmail(user.getEmail()).isPresent() ||
                userRepository.findByName(user.getName()).isPresent()) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        user.setActive(true);
        user = userRepository.save(user);
        sendWelcomeEmail(user);
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, @Valid User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void softDeleteMultipleUsers(List<Long> ids) {
        ids.forEach(this::softDeleteUser);
    }

    private void sendWelcomeEmail(User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("New User Registration");
        helper.setText("Welcome " + user.getName() + "!,\n\nYou have successfully signed up.");
        mailSender.send(message);
        log.info("Email sent ...");
    }

}
