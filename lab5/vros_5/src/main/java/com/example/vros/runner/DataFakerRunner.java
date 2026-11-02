package com.example.vros.runner;

import com.example.vros.entity.*;
import com.example.vros.repository.*;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        // generam datele o singura data
        if (deliveryRepository.count() > 0) {
            System.out.println("DataFaker: baza de date are deja date.");
            return;
        }

        Faker faker = new Faker();
        long start = System.currentTimeMillis();

        // Depouri
        List<Depot> depots = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Depot depot = new Depot(
                    "Depot " + i + " " + faker.address().city(),
                    faker.number().randomDouble(4, 44, 48),     // latitudine (Romania)
                    faker.number().randomDouble(4, 21, 29));    // longitudine (Romania)
            depots.add(depot);
        }
        depots = depotRepository.saveAll(depots);

        // Soferi
        List<Driver> drivers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Driver driver = new Driver(
                    faker.name().fullName(),
                    "driver" + i + "." + faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone());
            drivers.add(driver);
        }
        drivers = driverRepository.saveAll(drivers);

        // Vehicule
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
            Customer customer = new Customer(
                    faker.name().fullName(),
                    "customer" + i + "." + faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone(),
                    faker.address().fullAddress(),
                    faker.number().randomDouble(4, 44, 48),
                    faker.number().randomDouble(4, 21, 29));
            customers.add(customer);
        }
        customers = customerRepository.saveAll(customers);

        // Livrari
        List<Delivery> deliveries = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            Customer customer = customers.get(faker.number().numberBetween(0, customers.size()));
            Delivery delivery = new Delivery(
                    customer,
                    faker.number().numberBetween(1, 101),
                    faker.lorem().sentence());
            delivery.setCreatedAt(LocalDateTime.ofInstant(
                    faker.timeAndDate().past(60, TimeUnit.DAYS), ZoneId.systemDefault()));
            deliveries.add(delivery);
        }
        deliveryRepository.saveAll(deliveries);

        long time = System.currentTimeMillis() - start;
        System.out.println("DataFaker: date generate in " + time + " ms");
        System.out.println("Depouri: " + depotRepository.count());
        System.out.println("Soferi: " + driverRepository.count());
        System.out.println("Vehicule: " + vehicleRepository.count());
        System.out.println("Clienti: " + customerRepository.count());
        System.out.println("Livrari: " + deliveryRepository.count());
    }
}