package com.jb.cs.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jb.cs.data.ex.AlreadyPurchasedCouponException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.jb.cs.data.model.Client.NO_ID;

@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @ManyToMany(mappedBy = "coupons", cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonIgnore
    private List<Customer> customers;

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private int category;
    private int amount;
    private double price;
    private String description;
    private String imageURL;

    public Coupon() {
        this.customers = new ArrayList<>();
    }

    public Coupon(long id) {
        this.id = id;
    }

    public void addCustomer(Customer customer) throws AlreadyPurchasedCouponException {
        if (amount > 0) {
            customers.add(customer);
        } else throw new AlreadyPurchasedCouponException(String.format("All coupons %s were sold out!", description));
    }

    public void deleteCustomer(Customer customer) {
        if (customers.contains(customer)) {
            customers.remove(customer);
        }
    }


    public static Coupon empty() {
        return new Coupon(NO_ID);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @JsonIgnore
    public void setCompanyId(long company_id) {
        Company company = new Company();
        company.setId(company_id);
        this.company = company;
    }

    public long getCompanyId(){
        return company.getId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", company=" + company +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", category=" + category +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    public boolean similarCoupon(Coupon coupon){
        if (this.getTitle().equals(coupon.getTitle())
                && this.getCategory() == coupon.getCategory()
                && this.getPrice() == coupon.getPrice()
                && this.getDescription().equals(coupon.getDescription())
                && this.getImageURL().equals(coupon.getImageURL())) {
            return true;
        }
        return false;
    }
}
