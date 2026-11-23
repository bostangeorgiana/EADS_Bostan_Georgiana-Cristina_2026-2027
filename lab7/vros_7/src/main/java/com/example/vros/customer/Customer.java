package com.example.vros.customer;

import com.example.vros.common.Person;
import com.example.vros.delivery.Delivery;
import com.example.vros.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends Person {

    private String address;
    private Double latitude;
    private Double longitude;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Delivery> deliveries = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

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

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    @Override
    public String toString() {
        return "Customer{id=" + getId() + ", name='" + getName() + "', email='" + getEmail() + "'}";
    }
}