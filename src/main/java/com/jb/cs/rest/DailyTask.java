package com.jb.cs.rest;

import com.jb.cs.data.db.CompanyRepository;
import com.jb.cs.data.db.CouponRepository;
import com.jb.cs.data.db.UserRepository;
import com.jb.cs.data.ex.NoSuchCompanyException;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.model.Coupon;
import com.jb.cs.data.model.Customer;
import com.jb.cs.service.CompanyService;
import com.jb.cs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DailyTask implements Runnable {

    private static final long day = 86400000;
    private static boolean isWorking = false;
    private CouponRepository couponRepo;
    private ApplicationContext context;

    @Autowired
    public DailyTask(CouponRepository couponRepo, ApplicationContext context) {
        this.couponRepo = couponRepo;
        this.context = context;
    }

    @Override
    public void run() {
        isWorking = true;
        while (isWorking) {
            long coupon_id;
            for (Coupon coupon : couponRepo.findAll()) {
                coupon_id = coupon.getId();
                LocalDate endDate = coupon.getEndDate();
                if (LocalDate.now().isAfter(endDate)) {
                    CompanyService companyService = context.getBean(CompanyService.class);
                    try {
                        companyService.deleteCouponById(coupon_id);
                    } catch (NoSuchCompanyException e) {
                        e.printStackTrace();
                    } catch (NoSuchCouponException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(day);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopDailyTask() {
        isWorking = false;
    }
}
