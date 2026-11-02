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

    // modifica un client existent; Optional gol daca nu exista
    public Optional<Customer> update(Long id, Customer data) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        Customer customer = existing.get();
        customer.setName(data.getName());
        customer.setEmail(data.getEmail());
        customer.setPhone(data.getPhone());
        customer.setAddress(data.getAddress());
        customer.setLatitude(data.getLatitude());
        customer.setLongitude(data.getLongitude());
        return Optional.of(customerRepository.save(customer));
    }
}
