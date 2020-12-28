package com.jb.cs.service;

import com.jb.cs.data.db.*;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.ex.NoSuchCustomerException;
import com.jb.cs.data.model.*;
import com.jb.cs.rest.UserSystem;
import com.jb.cs.service.ex.UserIsNotExistException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AdminServiceImpl implements AdminService{

    private long admin_id;

    UserRepository userRepo;
    CustomerRepository customerRepo;
    CompanyRepository companyRepo;
    CouponRepository couponRepo;

    public AdminServiceImpl(UserRepository userRepo, CustomerRepository customerRepo,
                            CompanyRepository companyRepo, CouponRepository couponRepo, UserSystem system) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.companyRepo = companyRepo;
        this.couponRepo = couponRepo;
    }

    public void setAdmin_id(long admin_id) {
        this.admin_id = admin_id;
    }

    //GETTER'S FOR COMPANY
    @Override
    public Company getCompanyById(long company_id) {
        return companyRepo.findById(company_id).orElse(Company.empty());
    }
    @Override
    public Company getCompanyByName(String name) {
        return companyRepo.findByName(name).orElse(Company.empty());
    }
    @Override
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    //UPDATE
    @Override
    public void updateCompany(Company company) {
        companyRepo.save(company);
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) throws NoSuchCouponException {
        Optional<Coupon> optCoupon = couponRepo.findById(coupon.getId());
        if (!optCoupon.isPresent()) {
            throw new NoSuchCouponException(String.format("There is no coupon with such id: " + coupon.getId()));
        }
        Coupon newCoupon = optCoupon.get();
        newCoupon.setTitle(coupon.getTitle());
        newCoupon.setStartDate(coupon.getStartDate());
        newCoupon.setEndDate(coupon.getEndDate());
        newCoupon.setCategory(coupon.getCategory());
        newCoupon.setAmount(coupon.getAmount());
        newCoupon.setDescription(coupon.getDescription());
        newCoupon.setPrice(coupon.getPrice());
        newCoupon.setImageURL(coupon.getImageURL());
        couponRepo.save(newCoupon);
        return newCoupon;
    }

    @Override
    public Customer updateCustomer(Customer customer) throws NoSuchCustomerException {
        Optional<Customer> optCustomer = customerRepo.findById(customer.getId());
        if (!optCustomer.isPresent()){
            throw new NoSuchCustomerException(String.format("There is no customer with such id: " + customer.getId()));
        }
        Customer newCustomer = optCustomer.get();
        newCustomer.setFirstName(customer.getFirstName());
        newCustomer.setLastName(customer.getLastName());
        customerRepo.save(newCustomer);
        return newCustomer;
    }





    // GETTER'S FOR CUSTOMER
    @Override
    public Customer getCustomerById(long customer_id) {
        return customerRepo.findById(customer_id).orElse(Customer.empty());
    }
    @Override
    public Customer getCustomerByFirstName(String firstName) {
        return customerRepo.findByFirstName(firstName).orElse(Customer.empty());
    }
    @Override
    public Customer getCustomerByLastName(String lastName) {
        return customerRepo.findByLastName(lastName).orElse(Customer.empty());
    }

    @Override
    public List<Customer> getCustomersByCouponId(long coupon_id) {
        return customerRepo.findAllCustomersByCoupons(coupon_id);
    }


    // CUSTOMER
    // GETTER'S
    @Override
    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    // GETTER'S FOR COUPON
    @Override
    public Coupon getCouponById(long id) {
        return couponRepo.findById(id).orElse(Coupon.empty());
    }
    @Override
    public List<Coupon> getCouponsByDescription(String description) {
        return couponRepo.findAllByDescription(description);
    }
    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }
    @Override
    public List<Coupon> getCouponsByTitle(String title) {
        return couponRepo.findAllByTitle(title);
    }
    @Override
    public List<Coupon> getCouponsByCategory(int category) {
        return couponRepo.findAllByCategory(category);
    }
    @Override
    public List<Coupon> getCouponsBeforeEndDate(LocalDate endDate) {
        return couponRepo.findAllByEndDate(endDate);
    }
    @Override
    public List<Coupon> getCouponsBeforeStartDate(LocalDate startDate) {
        return couponRepo.findAllByEndDate(startDate);
    }
    @Override
    public List<Coupon> getCouponsByCompanyId(long company_id) {
        return couponRepo.findAllByCompanyId(company_id);
    }
    @Override
    public List<Coupon> getCustomerCouponsById(long customer_id) {
        return couponRepo.findAllByCustomerId(customer_id);
    }
    @Override
    public List<Coupon> getCouponsBelowPrice(double price) {
        return couponRepo.findCouponsByPriceGreaterThan(price);
    }
    @Override
    public List<Coupon> getCouponsAbovePrice(double price) {
        return couponRepo.findAllByPriceIsGreaterThan(price);
    }


    @Override
    public Coupon createCoupon(Coupon coupon, long company_id) {
        Company company = this.getCompanyById(company_id);
        if (coupon != null && company != null) {
            coupon.setId(0);
            company.add(coupon);
            couponRepo.save(coupon);
        }
        return Coupon.empty();
    }



    // DELETE

    @Override
    public void deleteCoupon(long id) throws NoSuchCouponException {
        Optional<Coupon> optCoupon = couponRepo.findById(id);
        if (optCoupon.isPresent()){
            Coupon coupon = optCoupon.get();

            for (Customer customer : customerRepo.findAll()) {
                if (customer.getCoupons().contains(coupon)) {
                    customer.getCoupons().remove(coupon);
                }
            }
            Company company = coupon.getCompany();
            List<Coupon> coupons = company.getCoupons();
            coupons.remove(coupon);
            company.setCoupons(coupons);
            couponRepo.deleteById(id);
            System.out.println("Coupon was deleted successfully!");
        } else {
            throw new NoSuchCouponException(String.format("Coupon with id#%d does not exist.", id));
        }
    }

    @Override
    public void deleteCustomersCoupon(long customerId, long couponId){
        Customer customer = customerRepo.findById(customerId).get();
        Coupon coupon = couponRepo.findById(couponId).get();
        coupon.getCustomers().remove(customer);
        couponRepo.save(coupon);
        customer.getCoupons().remove(coupon);
        customerRepo.save(customer);
    }

    @Override
    public void deleteCustomerById(long id) {
        Customer customer = customerRepo.findById(id).get();

        for (Coupon c: couponRepo.findAll()){
            if (c.getCustomers().contains(customer)){
                c.getCustomers().remove(customer);
                couponRepo.save(c);
            }
        }

        for (User user : userRepo.findAll()) {
            if (user.getClient().getId() == id){
                userRepo.delete(user);
            }
        }
        customerRepo.deleteById(id);
    }

    @Override
    public void deleteCompanyById(long id) throws UserIsNotExistException {
        Optional<Company> company = companyRepo.findById(id);
        if (!company.isPresent()) {
            throw new UserIsNotExistException(String.format("Can't delete company with such id#%d", id));
        }
        companyRepo.deleteById(id);
    }

//    @Override
//    public void deleteUserById(long id) throws UserIsNotExistException {
//        Optional<User> optUser = userRepo.findById(id);
//        if (!optUser.isPresent()) {
//            throw new UserIsNotExistException(String.format("Can't delete user with such id#%d", id));
//        }
//        userRepo.deleteById(id);
//    }






}
