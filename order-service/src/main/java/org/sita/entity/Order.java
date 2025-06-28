package org.sita.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    UUID userId;

    String recepientName;
    String recepientAddress;
    Instant time;

    @JsonManagedReference
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    List<Product> product = new ArrayList<>();

    public Order(UUID userId, List<Product> product){
        this.userId = userId;
        this.product = product;

    }

    public void addProduct(Product p) {
        if(product.isEmpty() || !product.contains(p)){
            product.add(p);
            p.setOrder(this);
        }
    }

    public void addProducts(List<Product> productsToAdd) {
        for (Product prod : productsToAdd) {
            addProduct(prod);
        }
    }
}
