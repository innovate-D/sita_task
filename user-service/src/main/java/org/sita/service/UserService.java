package org.sita.service;

import lombok.extern.slf4j.Slf4j;
import org.sita.dto.UserRequest;
import org.sita.dto.UserResponse;
import org.sita.entity.User;
import org.sita.exception.CustomException;
import org.sita.exception.UserNotFoundException;
import org.sita.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {

        if(userRequest.getUserName().isEmpty() || userRequest.getPassword().isEmpty() || userRequest.getEmail().isEmpty()){
            throw new CustomException("Input is empty, please fill all fields");
        }
        if (ifUserExists(userRequest.getUserName(), userRequest.getEmail()).isPresent()) {
            throw new CustomException("User already exists");
        } else {
            User user = new User(userRequest.getUserName(), userRequest.getPassword(),userRequest.getEmail());
            UUID id = userRepository.save(user).getId();
            log.info("user is created with id {}", id);
            return new UserResponse(id, Instant.now());
        }
    }

    public UserResponse getUser(UUID id) {

        log.info("retrieving user with id {}", id);
        return userRepository.findById(id).map(UserResponse::new
        ).orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserResponse deleteUser(UUID id) {

        log.info("checking user exists with id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(user.getId());
        log.info("User is deleted with id {}", id);
        return new UserResponse(id, "User deleted successfully");

    }

    public UserResponse updateUser(UUID id, UserRequest request) {

        log.info("checking user data exists with id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (!request.getUserName().isEmpty()) {
            user.setUserName(request.getUserName());
        }
        if (!request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        if (!request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        userRepository.save(user);
        log.info("user is updated with id {}", id);
        return new UserResponse(id, "User details is updated");
    }

    public Optional<User> ifUserExists(String userName, String email) {
        return userRepository.findByUserNameOrEmail(userName, email);
    }


}
