package com.example.vros.service;

import com.example.vros.entity.Customer;
import com.example.vros.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() { return customerRepository.findAll(); }
    public Optional<Customer> findById(Long id) { return customerRepository.findById(id); }
    public Customer save(Customer customer) { return customerRepository.save(customer); }
    public void deleteById(Long id) { customerRepository.deleteById(id); }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public List<Customer> searchByName(String keyword) {
        return customerRepository.findByNameContainingIgnoreCase(keyword);
    }
}