package org.sita.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sita.dto.OrderRequest;
import org.sita.dto.OrderResponse;
import org.sita.dto.UpdateRequest;
import org.sita.entity.Order;
import org.sita.entity.Product;
import org.sita.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderServiceController orderServiceController;

    private UUID orderId;
    private OrderRequest orderRequest;
    private UpdateRequest updateRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductName("Iphone15");
        product.setQuantity(50);
        orderId = UUID.randomUUID();
        orderRequest = new OrderRequest(orderId, List.of(product));
        updateRequest = new UpdateRequest("test-user", "test-address");
    }

    @Test
    void testCreateOrder() {

        OrderResponse orderResponse = new OrderResponse(orderId, Instant.now());
        when(orderService.createOrder(orderRequest)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderServiceController.postOrder(orderRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderResponse, response.getBody());
    }

    @Test
    void testGetOrder() {
        Order order = new Order();
        order.setId(orderId);
        order.setProduct(List.of(product));
        OrderResponse orderResponse = new OrderResponse(List.of(new Order()));
        when(orderService.getOrder(orderId)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderServiceController.getOrder(orderId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderResponse, response.getBody());
    }

    @Test
    void testUpdateOrder() {

        OrderResponse orderResponse = new OrderResponse(orderId,"Order is updated");
        when(orderService.updateOrder(orderId, updateRequest)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderServiceController.updateOrder(orderId, updateRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderResponse, response.getBody());
    }

    @Test
    void testDeleteOrder() {
        OrderResponse orderResponse = new OrderResponse(orderId,"Order deleted successfully");

        when(orderService.deleteOrder(orderId)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderServiceController.deleteOrder(orderId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderResponse, response.getBody());
    }
}
