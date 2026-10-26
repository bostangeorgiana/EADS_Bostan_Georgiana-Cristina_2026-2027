package com.example.vros.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends Person {

    private String address;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "customer")          // inverse side
    private List<Delivery> deliveries = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name, String email, String phone, String address,
                    Double latitude, Double longitude) {
        super(name, email, phone);
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public List<Delivery> getDeliveries() { return deliveries; }
    public void setDeliveries(List<Delivery> deliveries) { this.deliveries = deliveries; }

    @Override
    public String toString() {
        return "Customer{id=" + getId() + ", name='" + getName() + "', email='" + getEmail() + "'}";
    }
}