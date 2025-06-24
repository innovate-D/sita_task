package org.sita.controller;

import org.sita.dto.OrderRequest;
import org.sita.dto.OrderResponse;
import org.sita.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderServiceController {

    private final OrderService orderService;

    public OrderServiceController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<OrderResponse> postOrder(@RequestBody OrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createNewOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.updateOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.deleteAnOrder(id));
    }
}
