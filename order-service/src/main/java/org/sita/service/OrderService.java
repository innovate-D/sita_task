package org.sita.service;

import lombok.extern.slf4j.Slf4j;
import org.sita.dto.OrderRequest;
import org.sita.dto.OrderResponse;
import org.sita.entity.Order;
import org.sita.exception.CustomException;
import org.sita.exception.OrderNotFoundException;
import org.sita.repository.OrderServiceRepository;
import org.sita.repository.ProductPortfolioRepository;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final OrderServiceRepository orderServiceRepository;
    private final ProductPortfolioRepository productPortfolioRepository;
    private final RestClient restClient;
    private static final String USER_SERVICE_URL = "http://localhost:8083/users";

    public OrderService(OrderServiceRepository orderServiceRepository, ProductPortfolioRepository productPortfolioRepository, RestClient restClient, CacheManager cacheManager) {
        this.orderServiceRepository = orderServiceRepository;
        this.productPortfolioRepository = productPortfolioRepository;
        this.restClient = restClient;
    }

    private HttpStatusCode userExists(UUID id){

        log.info("Checking if user exists for id {} ",id);
        HttpStatusCode code = null;
        try {
            code = restClient.get()
                    .uri(USER_SERVICE_URL + "/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity()
                    .getStatusCode();
        }catch (HttpClientErrorException e){
            if(e.getStatusCode()== HttpStatus.BAD_REQUEST){
                throw new CustomException("Input is invalid");
            }
            if(e.getStatusCode()==HttpStatus.NOT_FOUND){
                throw new CustomException("User not found");
            }
        }catch (HttpServerErrorException e){
            if(e.getStatusCode()==HttpStatus.INTERNAL_SERVER_ERROR){
                throw new CustomException("Something unexpected occurred, please try again later.");
            }
        }
        return code;
    }

    public OrderResponse createOrder(OrderRequest request){

        if(!userExists(request.getUserId()).is2xxSuccessful()){
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
        order.addProducts(request.getProduct());
        order.setUserId(request.getUserId());
        var id = orderServiceRepository.save(order).getId();
        log.info("Order is saved with id {} ",id);
        return new OrderResponse(id, Instant.now());
    }

    public int getStock(String name) {
        return productPortfolioRepository.findStockByName(name);
    }

    public void updateStock(String productName,int stock) {
        productPortfolioRepository.updateStockByName(productName,stock);
        log.info("Stock updated for product");

    }

    public OrderResponse getOrder(UUID id){

        log.info("retrieving order with id {}", id);
        return orderServiceRepository.findById(id).map(OrderResponse::new
        ).orElseThrow(() -> new OrderNotFoundException(id));
    }

    public OrderResponse deleteOrder(UUID id){

        log.info("checking order exists with id {}", id);
        Order order = orderServiceRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        orderServiceRepository.deleteById(order.getId());
        log.info("Order is deleted with id {}", id);
        return new OrderResponse(id, "Order deleted successfully");
    }

}
