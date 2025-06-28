package org.sita.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.sita.entity.User;

import java.time.Instant;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserResponse {

    UUID id;
    Instant createdAt;
    User user;
    String message;

    public UserResponse(UUID id, Instant instant) {
        this.id = id;
        this.createdAt = instant;
    }

    public UserResponse(User user) {
        this.user = user;
    }

    public UserResponse(UUID id,String message){
        this.id = id;
        this.message = message;
    }


}
