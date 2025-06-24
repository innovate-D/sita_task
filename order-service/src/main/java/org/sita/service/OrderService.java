package org.sita.service;

import org.sita.dto.OrderRequest;
import org.sita.dto.OrderResponse;
import org.sita.entity.Order;
import org.sita.repository.OrderServiceRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderServiceRepository orderServiceRepository;

    public OrderService(OrderServiceRepository orderServiceRepository) {
        this.orderServiceRepository = orderServiceRepository;
    }

    public OrderResponse createNewOrder(OrderRequest request){
        Order order = new Order(request.getUserId(),request.getProduct(),request.getQuantity(),request.getPrice());
        return new OrderResponse(orderServiceRepository.save(order).getId());
    }

    public OrderResponse getOrderById(Long id){

    }

    public OrderResponse updateOrderById(Long id){

    }

    public OrderResponse deleteAnOrder(Long id){

    }
}
