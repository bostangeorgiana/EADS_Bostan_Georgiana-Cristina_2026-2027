package com.example.vros.runner;

import com.example.vros.entity.Customer;
import com.example.vros.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomerTestRunner implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    public CustomerTestRunner(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {

        if (customerRepository.count() == 0) {
            Customer c1 = new Customer("Ana Popescu", "ana@mail.com", "0711111111",
                    "Str. Lapusneanu 10, Iasi", 47.1615, 27.5837);
            Customer c2 = new Customer("Ion Ionescu", "ion@mail.com", "0722222222",
                    "Bd. Carol I 22, Iasi", 47.1740, 27.5714);

            customerRepository.save(c1);
            customerRepository.save(c2);
        }

        for (Customer c : customerRepository.findAll()) {
            System.out.println(c);
        }

        System.out.println("Total clienti: " + customerRepository.count());
    }
}