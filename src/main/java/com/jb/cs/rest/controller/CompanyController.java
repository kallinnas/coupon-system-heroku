package com.jb.cs.rest.controller;

import com.jb.cs.data.ex.NoSuchCompanyException;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.ex.WrongInputDataException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Coupon;
import com.jb.cs.rest.ClientSession;
import com.jb.cs.service.CompanyService;
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
public class CompanyController {
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CompanyController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    public ClientSession getSession(String token) {
        return tokensMap.get(token);
    }




    // GETTER'S FOR COMPANY
    @GetMapping("company/{token}/getCompanyById/{company_id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String token, @PathVariable long company_id){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        Company company = service.getCompanyById(company_id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("company/{token}/getCompany")
    public ResponseEntity<Company> getCompany(@PathVariable String token){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        Company company = service.getCompany();
        return ResponseEntity.ok(company);
    }

    @GetMapping("company/{token}/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCompanies(@PathVariable String token){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        List<Company> companies= service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }





    // UPDATE FOR COMPANY
    @PutMapping("company/{token}/updateCompany")
    public ResponseEntity<Company> updateCompanyName(@PathVariable String token,
                                                     @RequestBody Company company) throws NoSuchCompanyException {

        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CompanyService service = session.getCompanyService();
//      service.getCompany();
        service.updateCompany(company);   /*Don't forget to put in updated customer to the Repo!!*/
        return ResponseEntity.ok(company);
    }





    // GETTER'S FOR COUPON
    @GetMapping("company/{token}/getAllCoupons")
    public ResponseEntity<List<Coupon>> getAllCoupons(@PathVariable String token) {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        List<Coupon> coupons = service.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("company/{token}/companyCoupons/{id}")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons (@PathVariable String token,
                                                              @PathVariable long id){
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        List<Coupon> allCompanyCoupons = service.getAllCompanyCoupons(id);
        return ResponseEntity.ok(allCompanyCoupons);
    }


    // UPDATE FOR COUPON
    @PostMapping("company/{token}/createCoupon")
    public ResponseEntity<Coupon> createCoupon(
            @PathVariable String token,
            @RequestBody Coupon coupon) throws WrongInputDataException, NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CompanyService service = session.getCompanyService();
        Coupon newCoupon = service.createCoupon(coupon);
        if (newCoupon != null) {
            service.updateCoupon(newCoupon);
            return ResponseEntity.ok(newCoupon);
        }
        throw new WrongInputDataException("Couldn't create coupon.");
    }

    @DeleteMapping("company/{token}/deleteCouponById/{id}")
    public void deleteCouponById(
            @PathVariable String token,
            @PathVariable long id) throws NoSuchCouponException, NoSuchCompanyException {
        ClientSession session = getSession(token);
        CompanyService service = session.getCompanyService();
        service.deleteCouponById(id);
    }

    @PutMapping("company/{token}/updateCoupon")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon)
            throws NoSuchCouponException {
        ClientSession session = getSession(token);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CompanyService service = session.getCompanyService();
        Coupon updateCoupon = service.updateCoupon(coupon);
        if (updateCoupon != null) {
            return ResponseEntity.ok(updateCoupon);
        }
        throw new NoSuchCouponException("Failed to update coupon.");
    }


}
