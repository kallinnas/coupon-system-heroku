package com.jb.cs.data.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
public class Company extends Client {

    private String name;
    private String imageURL;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Coupon> coupons;

    public Company(){
        coupons = new ArrayList<>();
    }

    public Company(int id) {
        setId(id);
    }

    public void add(Coupon coupon) {
        coupon.setCompany((this));
        coupons.add(coupon);
    }

    public static Company empty() {
        return new Company(NO_ID);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }



}
