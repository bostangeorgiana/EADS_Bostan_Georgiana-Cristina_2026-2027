package com.example.vros.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer capacity;
    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "depot_id")
    private Depot depot;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public Vehicle() {
    }

    public Vehicle(String name, Integer capacity, Depot depot) {
        this.name = name;
        this.capacity = capacity;
        this.depot = depot;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public Depot getDepot() { return depot; }
    public void setDepot(Depot depot) { this.depot = depot; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    @Override
    public String toString() {
        return "Vehicle{id=" + id + ", name='" + name + "', capacity=" + capacity
                + ", available=" + available + "}";
    }
}