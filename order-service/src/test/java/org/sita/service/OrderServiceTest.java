package org.sita.service;

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
import org.sita.exception.CustomException;
import org.sita.repository.OrderServiceRepository;
import org.sita.repository.ProductPortfolioRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderServiceRepository orderServiceRepository;

    @Mock
    private ProductPortfolioRepository productPortfolioRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private OrderService orderService;

    private UUID userId;
    private OrderRequest orderRequest;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        product = new Product();
        product.setProductName("Iphone15");
        product.setQuantity(50);
        orderRequest = new OrderRequest(userId, List.of(product));
        order = new Order();
        order.setUserId(userId);
        order.addProducts(List.of(product));
    }

    @Test
    void testCreateOrder_Success() {

        when(orderClient.userExists(userId)).thenReturn(HttpStatus.OK);

        when(productPortfolioRepository.findStockByName("Iphone15")).thenReturn(100);

        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("Product")).thenReturn(cache);
        when(cache.get("Iphone15")).thenReturn(() -> 2000);

        order.setId(UUID.randomUUID());
        when(orderServiceRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(order.getId(), response.getId());
    }

    @Test
    void testGetOrder_NotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderServiceRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderResponse response = orderService.getOrder(orderId);

        assertEquals("No order found", response.getMessage());
    }

    @Test
    void testDeleteOrder_NoOrderFound() {
        UUID orderId = UUID.randomUUID();

        when(orderServiceRepository.findById(orderId)).thenReturn(Optional.empty());
        CustomException ex = assertThrows(CustomException.class, () -> orderService.deleteOrder(orderId));
        assertEquals("no order found", ex.getMessage());
    }


    @Test
    void testUpdateOrder_Success() {
        UUID orderId = UUID.randomUUID();
        UpdateRequest updateRequest = new UpdateRequest("New Name", "New Address");

        order.setId(orderId);
        when(orderServiceRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderServiceRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.updateOrder(orderId, updateRequest);

        assertEquals("Order is updated", response.getMessage());
    }
}
