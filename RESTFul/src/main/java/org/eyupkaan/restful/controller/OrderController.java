package org.eyupkaan.restful.controller;

import org.eyupkaan.restful.exception.OrderNotFoundException;
import org.eyupkaan.restful.repository.OrderRepository;
import org.eyupkaan.restful.model.Status;
import org.eyupkaan.restful.model.Order;
import org.eyupkaan.restful.model.OrderModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    OrderRepository repository;
    OrderModelAssembler assembler;

    OrderController(OrderRepository repository, OrderModelAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }
    @GetMapping()
    public CollectionModel<EntityModel<Order>> allOrders(){
        List<EntityModel<Order>> orders = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).allOrders()).withSelfRel());
    }
    @PostMapping()
    public ResponseEntity<EntityModel<Order>> addOrder(@RequestBody Order order){
        order.setStatus(Status.IN_PROGRESS);
        Order newOrder = repository.save(order);

        return ResponseEntity
                .created(
                        linkTo(methodOn(OrderController.class).getOrderById(newOrder.getId())).toUri()
                )
                .body(assembler.toModel(newOrder));
    }
    @GetMapping("/{id}")
    public EntityModel<Order> getOrderById(@PathVariable Long id){
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can not cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can not complete an order that is in the "+order.getStatus() +" status"));
    }
}
