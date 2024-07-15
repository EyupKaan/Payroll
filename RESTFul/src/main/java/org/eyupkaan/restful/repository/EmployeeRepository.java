package org.eyupkaan.restful.repository;

import org.eyupkaan.restful.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
