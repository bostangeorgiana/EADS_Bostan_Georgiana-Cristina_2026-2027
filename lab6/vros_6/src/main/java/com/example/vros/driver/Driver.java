package com.example.vros.driver;

import com.example.vros.common.Person;
import com.example.vros.user.AppUser;
import com.example.vros.vehicle.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver extends Person {

    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    public Driver() {
    }

    public Driver(String name, String email, String phone) {
        super(name, email, phone);
    }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    @Override
    public String toString() {
        return "Driver{id=" + getId() + ", name='" + getName() + "', email='" + getEmail() + "'}";
    }
}