package org.sita.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NonNull
    String userName;
    @NonNull
    @JsonIgnore
    String password;
    @NonNull
    String email;
}
