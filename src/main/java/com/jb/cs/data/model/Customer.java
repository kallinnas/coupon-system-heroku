package com.jb.cs.data.model;

import com.jb.cs.data.ex.AlreadyPurchasedCouponException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer extends Client {

    private String firstName;
    private String lastName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "customer_coupon",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private List<Coupon> coupons;

    public Customer() {
        this.coupons = new ArrayList<>();
    }

    public Customer(long id) {
        setId(id);
    }

    public static Customer empty() {
        return new Customer(NO_ID);
    }

    public void add(Coupon coupon) throws AlreadyPurchasedCouponException {
        coupon.addCustomer(this); /*"this" is reference for our coupon-"guy"*/
        coupons.add(coupon);
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }


}
