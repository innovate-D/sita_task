package org.sita.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.sita.entity.Order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OrderResponse {

    UUID id;
    Instant createdAt;
    String message;
    List<Order> order;

    public OrderResponse(UUID id, Instant createdAt){
        this.id = id;
        this.createdAt = createdAt;
    }

    public OrderResponse(UUID id, String message){
        this.id = id;
        this.message = message;
    }

    public OrderResponse(List<Order> order){
        this.order = order;
    }

    public OrderResponse(String message){
        this.message=message;
    }
}
