package org.sita.repository;

import org.sita.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderServiceRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserId(UUID userId);

    Optional<Order> findByIdAndUserId(UUID id, UUID userId);
}
