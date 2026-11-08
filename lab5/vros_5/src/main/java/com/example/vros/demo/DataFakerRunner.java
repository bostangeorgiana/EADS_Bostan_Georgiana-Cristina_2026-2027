package com.example.vros.demo;

import com.example.vros.customer.Customer;
import com.example.vros.customer.CustomerRepository;
import com.example.vros.delivery.Delivery;
import com.example.vros.delivery.DeliveryRepository;
import com.example.vros.depot.Depot;
import com.example.vros.depot.DepotRepository;
import com.example.vros.driver.Driver;
import com.example.vros.driver.DriverRepository;
import com.example.vros.vehicle.Vehicle;
import com.example.vros.vehicle.VehicleRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// DATAFAKER: umple baza de date cu date de test, o singura data (daca nu exista livrari)
@Component
public class DataFakerRunner implements CommandLineRunner {

    private final DepotRepository depotRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryRepository deliveryRepository;

    public DataFakerRunner(DepotRepository depotRepository, DriverRepository driverRepository,
                           VehicleRepository vehicleRepository, CustomerRepository customerRepository,
                           DeliveryRepository deliveryRepository) {
        this.depotRepository = depotRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void run(String... args) {
        if (deliveryRepository.count() > 0) {
            System.out.println("DataFaker: baza de date are deja date.");
            return;
        }

        Faker faker = new Faker();
        long start = System.currentTimeMillis();

        // Depouri
        List<Depot> depots = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            depots.add(new Depot(
                    "Depot " + i + " " + faker.address().city(),
                    faker.number().randomDouble(4, 44, 48),     // latitudine (Romania)
                    faker.number().randomDouble(4, 21, 29)));   // longitudine (Romania)
        }
        depots = depotRepository.saveAll(depots);

        // Soferi
        List<Driver> drivers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            drivers.add(new Driver(
                    faker.name().fullName(),
                    "driver" + i + "." + faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone()));
        }
        drivers = driverRepository.saveAll(drivers);

        // Vehicule (fiecare cu alt sofer)
        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Depot depot = depots.get(faker.number().numberBetween(0, depots.size()));
            Vehicle vehicle = new Vehicle("Vehicle " + i, faker.number().numberBetween(50, 501), depot);
            vehicle.setAvailable(faker.bool().bool());
            vehicle.setDriver(drivers.get(i - 1));
            vehicles.add(vehicle);
        }
        vehicleRepository.saveAll(vehicles);

        // Clienti
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            customers.add(new Customer(
                    faker.name().fullName(),
                    "customer" + i + "." + faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone(),
                    faker.address().fullAddress(),
                    faker.number().randomDouble(4, 44, 48),
                    faker.number().randomDouble(4, 21, 29)));
        }
        customers = customerRepository.saveAll(customers);

        // Livrari
        List<Delivery> deliveries = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            Customer customer = customers.get(faker.number().numberBetween(0, customers.size()));
            Delivery delivery = new Delivery(customer, faker.number().numberBetween(1, 101), faker.lorem().sentence());
            delivery.setCreatedAt(LocalDateTime.ofInstant(
                    faker.timeAndDate().past(60, TimeUnit.DAYS), ZoneId.systemDefault()));
            deliveries.add(delivery);
        }
        deliveryRepository.saveAll(deliveries);

        System.out.println("DataFaker: date generate in " + (System.currentTimeMillis() - start) + " ms");
    }
}
