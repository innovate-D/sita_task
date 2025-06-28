package org.sita.controller;

import org.sita.dto.OrderRequest;
import org.sita.dto.OrderResponse;
import org.sita.dto.UpdateRequest;
import org.sita.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderServiceController {

    private final OrderService orderService;

    public OrderServiceController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<OrderResponse> postOrder(@RequestBody OrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.getOrder(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable UUID id, @RequestBody UpdateRequest request ){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.updateOrder(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.deleteOrder(id));
    }
}
