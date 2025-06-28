package org.sita.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.sita.entity.Product;

import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderRequest {

    @NonNull
    UUID userId;

    @NonNull
    List<Product> product;
    String userName;
    String address;


}
