package org.sita.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Entity
@Table(name = "user_data")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    UUID userId;
    Product product;
    int quantity;
    int price;

    public Order(UUID userId, Product product, int quantity, int price){
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
