package org.sita.controller;

import jakarta.validation.Valid;
import org.sita.dto.UserRequest;
import org.sita.dto.UserResponse;
import org.sita.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUser(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.deleteUser(id));
    }
}
