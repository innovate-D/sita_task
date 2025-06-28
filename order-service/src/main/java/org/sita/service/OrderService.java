package org.sita.service;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final OrderServiceRepository orderServiceRepository;
    private final ProductPortfolioRepository productPortfolioRepository;
    private final OrderClient orderClient;
    private final CacheManager cacheManager;

    public OrderService(OrderServiceRepository orderServiceRepository, ProductPortfolioRepository productPortfolioRepository, OrderClient orderClient, CacheManager cacheManager) {
        this.orderServiceRepository = orderServiceRepository;
        this.productPortfolioRepository = productPortfolioRepository;
        this.orderClient = orderClient;
        this.cacheManager = cacheManager;
    }



    public OrderResponse createOrder(OrderRequest request){

        if(!orderClient.userExists(request.getUserId()).is2xxSuccessful()){
            throw new CustomException("Invalid user id");
        }
        if(request.getProduct().isEmpty()){
            throw new CustomException("Product is empty, Add at least one product");
        }

        request.getProduct().stream()
                .forEach(product -> {
                    int stock = getStock(product.getProductName());
                    if(stock>=product.getQuantity()){
                      updateStock(product.getProductName(),stock- product.getQuantity());
                    }else{
                        throw new CustomException("Insufficient product quantity");
                    }
                });

        Order order = new Order();
        for(Product p:request.getProduct()){
            Cache product = cacheManager.getCache("Product");
            Cache.ValueWrapper wrapper = product.get(p.getProductName());
            p.setPrice(p.getQuantity()*(Integer) wrapper.get());
        }
        order.addProducts(request.getProduct());
        order.setUserId(request.getUserId());
        order.setTime(Instant.now());
        var id = orderServiceRepository.save(order).getId();
        log.info("Order is saved with id {} ",id);
        return new OrderResponse(id, Instant.now());
    }

    public int getStock(String name) {
        return productPortfolioRepository.findStockByName(name);
    }

    private void updateStock(String productName,int stock) {
        productPortfolioRepository.updateStockByName(productName,stock);
        log.info("Stock updated for product");

    }

    public OrderResponse getOrder(UUID orderId){

        // show single order
        log.info("retrieving order with order-id {} ", orderId);
        Optional<Order> order = orderServiceRepository.findById(orderId);
        return order.map(value -> new OrderResponse(List.of(value))).orElseGet(() -> new OrderResponse("No order found"));
    }

    public OrderResponse deleteOrder(UUID orderId){

        OrderResponse response = getOrder(orderId);

        if(response.getOrder()==null){
            throw new CustomException("no order found");
        }

        orderServiceRepository.deleteById(orderId);
        log.info("Order is deleted with id {}", orderId);

        for(Product p : response.getOrder().getFirst().getProduct()){
            updateStock(p.getProductName(),getStock(p.getProductName())+p.getQuantity());
        }

        log.info("Stock is updated");
        return new OrderResponse(orderId, "Order deleted successfully");
    }


    public OrderResponse updateOrder(UUID orderId,UpdateRequest request){
        OrderResponse response = null;
        if(orderId!=null) {
           response  = getOrder(orderId);
        }else{
            throw new CustomException("Insufficient input, orderId should be filled");
        }

        if(response.getOrder().isEmpty()){
            throw new CustomException("no order found");
        }

        Order order = response.getOrder().getFirst();
        if(request.getRecepientAddress()!=null){
            order.setRecepientAddress(request.getRecepientAddress());
        }
        if(request.getRecepientName()!=null){
            order.setRecepientName(request.getRecepientName());
        }

        orderServiceRepository.save(order);
        log.info("Order is updated for order-id {} ",orderId);
        return new OrderResponse(orderId,"Order is updated");
    }

}
