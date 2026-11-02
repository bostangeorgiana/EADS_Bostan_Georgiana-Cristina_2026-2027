package com.example.vros.runner;

import com.example.vros.service.DeliveryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CriteriaSearchRunner implements CommandLineRunner {

    private final DeliveryService deliveryService;

    public CriteriaSearchRunner(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void run(String... args) {

        System.out.println("Fara filtre: "
                + deliveryService.search(null, null, null, null, null, null, null).size());

        System.out.println("Doar clientul cu id 3: "
                + deliveryService.search(3L, null, null, null, null, null, null).size());

        System.out.println("Doar zona (lat 45-46, lon 25-27): "
                + deliveryService.search(null, 45.0, 46.0, 25.0, 27.0, null, null).size());

        System.out.println("Doar cantitate > 90: "
                + deliveryService.search(null, null, null, null, null, 90, null).size());

        LocalDate date = LocalDate.now().minusDays(10);
        System.out.println("Doar data " + date + ": "
                + deliveryService.search(null, null, null, null, null, null, date).size());

        System.out.println("Combinat: zona + cantitate > 50: "
                + deliveryService.search(null, 45.0, 46.0, 25.0, 27.0, 50, null).size());

    }
}