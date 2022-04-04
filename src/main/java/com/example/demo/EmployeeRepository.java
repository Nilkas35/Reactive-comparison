package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "select repeat('employee',2000000)", nativeQuery = true)
    String getLargeData();
}
