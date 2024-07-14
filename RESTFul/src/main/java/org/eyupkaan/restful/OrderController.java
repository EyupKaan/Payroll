package org.eyupkaan.restful;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    OrderRepository repository;
    //TODO create an order assembler

    OrderController(OrderRepository repository){
        this.repository = repository;
    }
}
