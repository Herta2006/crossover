package com.dev.repositories;

import com.dev.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLinesRepository extends JpaRepository<OrderLine, Long> {
}
