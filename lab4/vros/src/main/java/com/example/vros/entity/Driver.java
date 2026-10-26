package com.example.vros.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver extends Person {

    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;

    public Driver() {
    }

    public Driver(String name, String email, String phone) {
        super(name, email, phone);
    }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    @Override
    public String toString() {
        return "Driver{id=" + getId() + ", name='" + getName() + "', email='" + getEmail() + "'}";
    }
}