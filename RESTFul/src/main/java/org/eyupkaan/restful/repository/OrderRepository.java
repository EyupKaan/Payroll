package org.eyupkaan.restful.repository;

import org.eyupkaan.restful.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
