package org.eyupkaan.restful;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(EmployeeController.class).getById(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).allEmployee()).withRel("employees")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).allEmployee()).withSelfRel());

    }
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee){
        return repository.save(employee);
    }
    @GetMapping("/employees/{id}")
    public EntityModel<Employee> getById(@PathVariable Long id){
        Employee employee = repository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException(id));

        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).allEmployee()).withRel("employees"));

    }
    @PutMapping("/employees/{id}")
    public Employee editEmployee(@RequestBody Employee employee,@PathVariable Long id){
        return repository.findById(id)
                .map(updated -> {
                    updated.setName(employee.getName());
                    updated.setRole(employee.getRole());
                    return repository.save(updated);
                })
                .orElseGet(() -> {
                    return repository.save(employee);
                });
    }
    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
    }
}