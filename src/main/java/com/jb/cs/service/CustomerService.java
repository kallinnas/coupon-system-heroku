package com.jb.cs.service;

import com.jb.cs.data.ex.AlreadyPurchasedCouponException;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.ex.NoSuchCustomerException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Coupon;
import com.jb.cs.data.model.Customer;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {

    void setCustomer_id(long customer_id);


    // GETTER'S & UPDATE FOR CUSTOMER
    Customer getCustomer();
    Customer updateCustomer(Customer customer) throws NoSuchCustomerException;


    // GETTER'S FOR COMPANY
    List<Company> getAllCompanies();
    Company getCompanyById(long id);


    // UPDATE'S FOR COUPON
    Coupon purchaseCoupon(long coupon_id) throws NoSuchCouponException, AlreadyPurchasedCouponException;
    Coupon releaseCoupon(long coupon_id) throws NoSuchCouponException;


    // GETTER'S FOR COUPON
    List<Coupon> getAllCompanyCoupons(long id);
    Coupon getCoupon(long coupon_id) throws NoSuchCouponException;
    List<Coupon> getAllCoupons();
    List<Coupon> getAllCustomerCoupons();
    List<Coupon> getCouponsByCompanyName(String name);
    List<Coupon> getCouponsByCategory(int category);
    List<Coupon> getCouponsByTitle(String title);
    List<Coupon> getCouponsBelowPrice(double price);
    List<Coupon> getCouponsAbovePrice(double price);
    List<Coupon> getCouponsInPriceRange(double fromPrice, double toPrice);

    void deleteCustomerItSelf(long id) throws NoSuchCustomerException;
}
