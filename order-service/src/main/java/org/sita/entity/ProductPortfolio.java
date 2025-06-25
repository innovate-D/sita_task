package org.sita.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@Table(name = "product_data")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String productName;
    int stock;
    int price;

    public ProductPortfolio(String productName,int stock,int price){
        this.productName=productName;
        this.stock=stock;
        this.price=price;
    }

}
