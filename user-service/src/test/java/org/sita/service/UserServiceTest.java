package org.sita.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sita.dto.UserRequest;
import org.sita.dto.UserResponse;
import org.sita.entity.User;
import org.sita.exception.CustomException;
import org.sita.exception.UserNotFoundException;
import org.sita.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequest request;
    private User user;
    private UUID userId;


    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        request = new UserRequest("test-user", "test-password", "test@sita.com");
        user = new User(request.getUserName(), request.getPassword(), request.getEmail());
        user.setId(userId);
    }


    @Test
    void testCreateUser_Success() {
        when(userRepository.findByUserNameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertEquals(userId, response.getId());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void testCreateUser_EmptyFields() {
        UserRequest emptyRequest = new UserRequest("", "", "");

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.createUser(emptyRequest);
        });
        assertEquals("Input is empty, please fill all fields", exception.getMessage());
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        when(userRepository.findByUserNameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.createUser(request);
        });

        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void testGetUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUser(userId);

        assertEquals(userId, response.getUser().getId());
    }

    @Test
    void testGetUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
        assertEquals("User deleted successfully", response.getMessage());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(userId, request);

        assertEquals("User details is updated", response.getMessage());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, request));
    }

}
