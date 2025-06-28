package org.sita.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sita.dto.UserRequest;
import org.sita.dto.UserResponse;
import org.sita.entity.User;
import org.sita.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userRequest = new UserRequest("test-user", "test-pass", "test@sita.com");
    }

    @Test
    void testCreateUser() {
        UserResponse userResponse = new UserResponse(userId, Instant.now());
        when(userService.createUser(userRequest)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.createUser(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void testGetUser() {
        UserResponse userResponse = new UserResponse(new User("test-user","test-pass","test@sita.com"));
        when(userService.getUser(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUser(userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void testUpdateUser() {
        UserResponse userResponse = new UserResponse(userId,"User details is updated");
        when(userService.updateUser(userId, userRequest)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.updateUser(userId, userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void testDeleteUser() {
        UserResponse userResponse = new UserResponse(userId,"User deleted successfully");
        when(userService.deleteUser(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }
}
