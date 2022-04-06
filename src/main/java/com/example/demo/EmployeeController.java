package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;
    private final CartRepository cartRepository;

    EmployeeController(EmployeeRepository repository, CartRepository cartRepository) {
        this.repository = repository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/employee")
    List<Employee> all() {
        return repository.findAll();
    }

    @GetMapping("/employee/big")
    List<Object> largeData() {
        List<Object> combined = new ArrayList<>();
        combined.add(repository.getLargeData());
        return combined;
    }

    @GetMapping("/employee/{idOne}/{idTwo}")
    List<Object> getBoth(@PathVariable Long idOne,@PathVariable Long idTwo) {
        Optional<Employee> employees = repository.findById(idOne);
        Optional<Cart> carts = cartRepository.findById(idTwo);
        List<Object> combined = new ArrayList<>();
        combined.add(employees);
        combined.add(carts);
        return combined;
    }

    @PostMapping("/employee")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employee/{id}")
    Employee one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employee/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employee/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}