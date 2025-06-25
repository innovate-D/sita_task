package org.sita.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.sita.entity.Product;
import org.sita.entity.ProductPortfolio;

import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    UUID userId;
    List<Product> product;


}
