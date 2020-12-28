package com.jb.cs.rest.controller;

import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.ex.NoSuchCustomerException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Coupon;
import com.jb.cs.data.model.Customer;
import com.jb.cs.rest.ClientSession;
import com.jb.cs.service.AdminService;
import com.jb.cs.service.ex.UserIsNotExistException;
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
public class AdminController {
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public AdminController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token) {
        return tokensMap.get(token);
    }


    // COUPON

    //GETTER'S
    @GetMapping("admin/{token}/getAllCoupons")
    public ResponseEntity<List<Coupon>> getAllCoupons(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Coupon> coupons = service.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("admin/{token}/getCompanyCoupons/{id}")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons(@PathVariable String token,
                                                             @PathVariable long id) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Coupon> coupons = service.getCouponsByCompanyId(id);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("admin/{token}/getCustomerCoupons/{id}")
    public ResponseEntity<List<Coupon>> getCustomerCoupons(@PathVariable String token,
                                                           @PathVariable long id){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Coupon> coupons = service.getCustomerCouponsById(id);
        return ResponseEntity.ok(coupons);
    }

    // CUSTOMER
    // GETTER'S
    @GetMapping("admin/{token}/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers(@PathVariable String token){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Customer> customers = service.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("admin/{token}/getCustomerById/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String token,
                                                    @PathVariable long id){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Customer customer = service.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }


    // COMPANY
    // GETTER'S
    @GetMapping("admin/{token}/companies")
    public ResponseEntity<List<Company>> getAllCompanies(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        List<Company> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("admin/{token}/getCompanyById/{company_id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String token, @PathVariable long company_id) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        Company company = service.getCompanyById(company_id);
        return ResponseEntity.ok(company);
    }

    // UPDATE
    @PutMapping("admin/{token}/updateCompany")
    public ResponseEntity<Company> updateCompany(@PathVariable String token,
                                                 @RequestBody Company company) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.updateCompany(company);   /*Don't forget to put in updated customer to the Repo!!*/
        return ResponseEntity.ok(company);
    }

    @PutMapping("admin/{token}/updateCoupon")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String token,
                                               @RequestBody Coupon coupon) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        return ResponseEntity.ok(service.updateCoupon(coupon));
    }

    @PutMapping("admin/{token}/updateCustomer")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String token,
                                                   @RequestBody Customer customer) throws NoSuchCustomerException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        return ResponseEntity.ok(service.updateCustomer(customer));
    }


    // DELETE
    @DeleteMapping("admin/{token}/deleteCoupon/{id}")
    public void deleteCoupon(@PathVariable String token,
                             @PathVariable long id) throws NoSuchCouponException {
        ClientSession session = getSession(token);
        AdminService service = session.getAdminService();
        service.deleteCoupon(id);
    }

    @DeleteMapping("admin/{token}/deleteCustomersCoupon/{customerId}/{couponId}")
    public void deleteCustomersCoupon(@PathVariable String token,
                                                        @PathVariable long customerId,
                                                        @PathVariable long couponId){
        ClientSession session = getSession(token);
        AdminService service = session.getAdminService();
        service.deleteCustomersCoupon(customerId, couponId);
    }

    @DeleteMapping("admin/{token}/deleteCompanyById/{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable String token,
                                                    @PathVariable long id) throws UserIsNotExistException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AdminService service = session.getAdminService();
        service.deleteCompanyById(id);
        String ok = "Company was deleted successfully!";
        return ResponseEntity.ok(ok);
    }

    @DeleteMapping("admin/{token}/deleteCustomerById/{id}")
    public void deleteCustomerById(@PathVariable String token,
                                   @PathVariable long id) {
        ClientSession session = getSession(token);
        AdminService service = session.getAdminService();
        service.deleteCustomerById(id);

    }

    @DeleteMapping("admin/{token}/deleteUserById/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String token, @PathVariable long id) throws UserIsNotExistException {
        ClientSession session = getSession(token);
        if (session == null) {
            System.out.println("Unauthorized session!");
        }
        AdminService service = session.getAdminService();
        service.deleteCompanyById(id);
        String ok = "User was deleted successfully!";
        return ResponseEntity.ok(ok);
    }






}
