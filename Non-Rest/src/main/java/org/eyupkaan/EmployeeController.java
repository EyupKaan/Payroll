package org.eyupkaan;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/employees")
    public List<Employee> allEmployee(){
        return repository.findAll();
    }
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee){
        return repository.save(employee);
    }
    @GetMapping("/employees/{id}")
    public Employee getById(@PathVariable Long id){
        return repository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException(id)
        );
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
