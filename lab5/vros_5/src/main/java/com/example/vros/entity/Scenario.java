package com.example.vros.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "scenarios")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "scenario_vehicles",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "scenario_deliveries",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "delivery_id"))
    private Set<Delivery> deliveries = new HashSet<>();

    public Scenario() {
    }

    public Scenario(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(Set<Vehicle> vehicles) { this.vehicles = vehicles; }

    public Set<Delivery> getDeliveries() { return deliveries; }
    public void setDeliveries(Set<Delivery> deliveries) { this.deliveries = deliveries; }

    @Override
    public String toString() {
        return "Scenario{id=" + id + ", name='" + name + "'}";
    }
}
