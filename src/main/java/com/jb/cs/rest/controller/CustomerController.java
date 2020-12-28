package com.jb.cs.rest.controller;

import com.jb.cs.data.ex.AlreadyPurchasedCouponException;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.ex.NoSuchCustomerException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Coupon;
import com.jb.cs.data.model.Customer;
import com.jb.cs.rest.ClientSession;
import com.jb.cs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/")
public class CustomerController {

    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CustomerController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    private ClientSession getSession(String token) {
        return tokensMap.get(token);
    }

    // GETTER'S CUSTOMER
    @GetMapping("customer/{token}/getCustomer")
    public ResponseEntity<Customer> getCustomer(@PathVariable String token){
        ClientSession session = getSession(token);
        Customer customer = session.getCustomerService().getCustomer();
        return ResponseEntity.ok(customer);
    }

    // GETTER'S FOR COMPANY
    @GetMapping("customer/{token}/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCompanies(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Company> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("customer/{token}/getCompanyById/{company_id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String token, @PathVariable long company_id){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Company company = service.getCompanyById(company_id);
        return ResponseEntity.ok(company);
    }




    //USE COUPON
    @PostMapping("customer/{token}/buyCoupon/{coupon_id}")
    public ResponseEntity<Coupon> purchaseCoupon(@PathVariable String token,
            @PathVariable long coupon_id) throws NoSuchCouponException, AlreadyPurchasedCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomerService service = session.getCustomerService();
        Coupon coupon = service.purchaseCoupon(coupon_id);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping("customers/{token}/useCoupon/{coupon_id}")
    public ResponseEntity<Coupon> releaseCoupon(
            @PathVariable String token,
            @PathVariable long coupon_id) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Coupon coupon = service.releaseCoupon(coupon_id);
        return ResponseEntity.ok(coupon);
    }

    // DELETE CUSTOMER
    @DeleteMapping("customer/{token}/deleteCustomerById/{id}")
    public void deleteCustomerById(@PathVariable String token, @PathVariable long id) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        CustomerService service = session.getCustomerService();
        service.deleteCustomerItSelf(id);
    }




    // GETTER'S FOR COUPON
    @GetMapping("customer/{token}/companyCoupons/{id}")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons (@PathVariable String token,
                                                              @PathVariable long id){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> allCompanyCoupons = service.getAllCompanyCoupons(id);
        return ResponseEntity.ok(allCompanyCoupons);
    }

    @GetMapping("customers/{token}/get/{coupon_id}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable String token,
                                            @PathVariable long coupon_id) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Coupon coupon = service.getCoupon(coupon_id);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("customer/{token}/getAllCoupons")
    public ResponseEntity<List<Coupon>> getAllCoupons(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/myCoupons")
    public ResponseEntity<List<Coupon>> getCustomerCoupons(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getAllCustomerCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/companyName{name}")
    public ResponseEntity<List<Coupon>> getCouponsByCompanyName(@PathVariable String token,
                                                                @PathVariable String name) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Coupon> coupons = session.getCustomerService().getCouponsByCompanyName(name);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/category/{category}")
    public ResponseEntity<List<Coupon>> getCouponsByCategory(@PathVariable String token,
                                                             @PathVariable int category) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getCouponsByCategory(category);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/title/{title}")
    public ResponseEntity<List<Coupon>> getCouponsByTitle(@PathVariable String token,
                                                          @PathVariable String title) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getCouponsByTitle(title);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/belowPrice/{price}")
    public ResponseEntity<List<Coupon>> getCouponsBelowPrice(@PathVariable String token,
                                                             @PathVariable double price) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getCouponsBelowPrice(price);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/abovePrice/{price}")
    public ResponseEntity<List<Coupon>> getCouponsAbovePrice(@PathVariable String token,
                                                             @PathVariable double price) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getCouponsAbovePrice(price);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("customer/{token}/inPriceRange/{from}-{to}")
    public ResponseEntity<List<Coupon>> getCouponsInPriceRange (@PathVariable String token,
                                                                @PathVariable double from,
                                                                @PathVariable double to) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        List<Coupon> coupons = service.getCouponsInPriceRange(from, to);
        return ResponseEntity.ok(coupons);
    }




    // UPDATE FOR CUSTOMER
    @PutMapping("customer/{token}/updateCustomer")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String token,
                                                   @RequestBody Customer customer) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerService service = session.getCustomerService();
        Customer c = service.updateCustomer(customer);
        return ResponseEntity.ok(c);
    }







}

