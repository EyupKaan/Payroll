package org.eyupkaan.restful;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }
    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> allEmployee(){

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).allEmployee()).withSelfRel());

    }
    @PostMapping("/employees")
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee){
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(employee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    @GetMapping("/employees/{id}")
    public EntityModel<Employee> getById(@PathVariable Long id){
        Employee employee = repository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);

    }
    @PutMapping("/employees/{id}")
    public ResponseEntity<?> editEmployee(@RequestBody Employee employee,@PathVariable Long id){
        Employee updatedModel = repository.findById(id)
                .map(updated -> {
                    updated.setName(employee.getName());
                    updated.setRole(employee.getRole());
                    return repository.save(updated);
                })
                .orElseGet(() -> {
                    return repository.save(employee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(updatedModel);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}