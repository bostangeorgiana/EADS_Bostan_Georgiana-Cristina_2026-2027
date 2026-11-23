package com.example.vros.customer;

import com.example.vros.common.ResourceNotFoundException;
import com.example.vros.delivery.Delivery;
import com.example.vros.delivery.DeliveryDto.DeliveryResponse;
import com.example.vros.delivery.DeliveryService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/customers")
public class CustomerControllerV2 {

    private final CustomerService customerService;
    private final DeliveryService deliveryService;

    public CustomerControllerV2(CustomerService customerService, DeliveryService deliveryService) {
        this.customerService = customerService;
        this.deliveryService = deliveryService;
    }

    // GET /api/v2/customers/{id} -> clientul + link-uri (self, deliveries, customers)
    @GetMapping("/{id}")
    public EntityModel<Customer> getById(@PathVariable Long id) {
        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + id + " nu exista"));
        return toModel(customer);
    }

    // GET /api/v2/customers/{id}/deliveries -> livrarile clientului (resursa legata)
    @GetMapping("/{id}/deliveries")
    public CollectionModel<DeliveryResponse> getDeliveries(@PathVariable Long id) {
        customerService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + id + " nu exista"));

        List<DeliveryResponse> deliveries = new ArrayList<>();
        for (Delivery delivery : deliveryService.findByCustomer(id)) {
            deliveries.add(DeliveryResponse.from(delivery));
        }

        return CollectionModel.of(deliveries,
                linkTo(methodOn(CustomerControllerV2.class).getDeliveries(id)).withSelfRel(),
                linkTo(methodOn(CustomerControllerV2.class).getById(id)).withRel("customer"));
    }

    // GET /api/v2/customers -> toti clientii, fiecare cu link-urile lui
    @GetMapping
    public CollectionModel<EntityModel<Customer>> getAll() {
        List<EntityModel<Customer>> customers = new ArrayList<>();
        for (Customer customer : customerService.findAll()) {
            customers.add(toModel(customer));
        }
        return CollectionModel.of(customers,
                linkTo(methodOn(CustomerControllerV2.class).getAll()).withSelfRel());
    }

    // "ambaleaza" un client si ii adauga link-urile
    private EntityModel<Customer> toModel(Customer customer) {
        return EntityModel.of(customer,
                linkTo(methodOn(CustomerControllerV2.class).getById(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomerControllerV2.class).getDeliveries(customer.getId())).withRel("deliveries"),
                linkTo(methodOn(CustomerControllerV2.class).getAll()).withRel("customers"));
    }
}