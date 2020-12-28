package com.jb.cs.service;

import com.jb.cs.data.ex.NoSuchCompanyException;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Coupon;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyService {

    // GETTER'S FOR COMPANY
    Company getCompanyById(long id);
    Company getCompany();
    List<Company> getAllCompanies();

    // UPDATE COMPANY
    void updateCompany(Company company) throws NoSuchCompanyException;
    void setCompany_id(long id);


    // GETTER'S FOR COUPON
    List<Coupon> getAllCoupons();
    List<Coupon> getAllCompanyCoupons(long id);

    // UPDATE FOR COUPON
    Coupon createCoupon(Coupon coupon);
    Coupon updateCoupon(Coupon coupon) throws NoSuchCouponException;
    void deleteCouponByCompanyId(long id);

    @Transactional
    public void deleteCouponById(long coupon_id) throws NoSuchCompanyException, NoSuchCouponException;


//    List<Coupon> getCouponsByCategory(int category);
//    List<Coupon> getCouponsByEndDate(LocalDate endDate);
//    List<Coupon> getCouponsByStartDate(LocalDate startDate);
//    List<Coupon> getCouponsByDescription(String description);

}
