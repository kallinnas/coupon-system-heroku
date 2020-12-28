package com.jb.cs.data.db;

import com.jb.cs.data.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByCompanyName(String name);
    List<Coupon> findAllByDescription(String description);
    List<Coupon> findAllByTitle(String title);
    List<Coupon> findAllByCategory(int category);
    List<Coupon> findCouponsByPriceGreaterThan(double price);
    List<Coupon> findAllByPriceIsGreaterThan(double price);
    List<Coupon> findAllByPriceBetween(double fromPrice, double toPrice);
    List<Coupon> findAllByEndDate(LocalDate date);
    List<Coupon> findAllByEndDateBefore(LocalDate date);

    void deleteById(long coupon_id);

    @Query("select coupon from Customer as customer join customer.coupons as coupon where customer.id=:customer_id")
    List<Coupon> findAllByCustomerId(long customer_id);

    @Query("select coupon from Company as company join company.coupons as coupon where company.id=:company_id")
    List<Coupon> findAllByCompanyId(long company_id);

    @Query("select coupon from Company as company join company.coupons as coupon where company.id = :company_id and coupon.category = :category")
    List<Coupon> findCouponsByCompanyIdAndCategory(long company_id, int category);


}
