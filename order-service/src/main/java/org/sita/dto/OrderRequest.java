package org.sita.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.sita.entity.Product;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    UUID userId;
    Product product;
    int quantity;
    int price;

}
