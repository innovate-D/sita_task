package org.sita.dto;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    String userName;
    String password;

    @Email(message = "Invalid email")
    String email;
}
