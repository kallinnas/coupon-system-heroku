package com.jb.cs.service;

import com.jb.cs.data.db.CompanyRepository;
import com.jb.cs.data.db.CouponRepository;
import com.jb.cs.data.db.CustomerRepository;
import com.jb.cs.data.db.UserRepository;
import com.jb.cs.data.ex.AlreadyPurchasedCouponException;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.ex.NoSuchCustomerException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Coupon;
import com.jb.cs.data.model.Customer;
import com.jb.cs.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerServiceImpl implements CustomerService {

    private long customer_id;
    private CouponRepository couponRepo;
    private CustomerRepository customerRepo;
    private CompanyRepository companyRepo;
    private UserRepository userRepo;

    @Autowired
    public CustomerServiceImpl(CouponRepository couponRepository,CompanyRepository companyRepository,
                               CustomerRepository customerRepository, UserRepository userRepository) {
        this.couponRepo = couponRepository;
        this.customerRepo = customerRepository;
        this.companyRepo = companyRepository;
        this.userRepo = userRepository;
    }

    /*Set User in UserSystem*/
    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }




    // GETTER'S & UPDATE FOR CUSTOMER
    @Override
    public Customer getCustomer() {
        Customer customer = customerRepo.findById(customer_id).get();
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) throws NoSuchCustomerException {
        Customer customerFromRepo = customerRepo.findById(customer.getId()).get();
        customerFromRepo.setFirstName(customer.getFirstName());
        customerFromRepo.setLastName(customer.getLastName());
        customerRepo.save(customerFromRepo);
        return customerFromRepo;
    }




    // GETTER'S FOR COMPANY
    @Override
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    @Override
    public Company getCompanyById(long id) {
        return companyRepo.findById(id).orElse(Company.empty());
    }







    // UPDATE'S FOR COUPON
    @Override
    public Coupon purchaseCoupon(long coupon_id) throws NoSuchCouponException, AlreadyPurchasedCouponException {
        Optional<Coupon> optCoupon = couponRepo.findById(coupon_id);
        Optional<Customer> optCustomer = customerRepo.findById(this.customer_id);
        Coupon coupon;
        Customer customer;

        if (optCoupon.isPresent() && optCustomer.isPresent()) {
            coupon = optCoupon.get();
            customer = optCustomer.get();

            /*For limiting our customer to buy a second coupon with same id*/
            for (Coupon c : customer.getCoupons()) {
                if (coupon.getId() == c.getId()) {
                    throw new AlreadyPurchasedCouponException(String.format(
                            "You already have coupon with id#%d", coupon_id));
                }
            }

            customer.add(coupon);         /*  add is adding as well customer to coupon's List of Customers*/
            coupon.setAmount(coupon.getAmount()-1); /*-1 coupon to the coupon's repository*/
            couponRepo.save(coupon);
            customerRepo.save(customer);    /*saving new owner of the coupon to the customer's repository*/
            return coupon;

        }
        throw new NoSuchCouponException(String.format("Couldn't find coupon with such id #%d.", coupon_id));
    }

    @Override
    public Coupon releaseCoupon(long coupon_id) throws NoSuchCouponException {
        Coupon coupon = getCoupon(coupon_id);
        List<Coupon> coupons = couponRepo.findAllByCustomerId(customer_id);
        Optional<Customer> optCustomer = customerRepo.findById(customer_id);
        if (coupons.contains(coupon) && optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            coupon.getCustomers().remove(customer); //delete customer from customer's list of specified coupon
            couponRepo.save(coupon);
            coupons.remove(getCoupon(coupon_id)); //delete coupon from list coupons that belongs to customer
            customer.setCoupons(coupons);
            customerRepo.save(customer);
            System.out.println("Coupon was released successfully!");
            return coupon;
        }
        String message = "No such coupon to release.";
        throw new NoSuchCouponException(message);
    }






    // GETTER'S FOR COUPON
    @Override
    public List<Coupon> getAllCompanyCoupons(long id) {
        return couponRepo.findAllByCompanyId(id);
    }

    @Override
    public Coupon getCoupon(long coupon_id) throws NoSuchCouponException {
        Optional<Coupon> optCoupon = couponRepo.findById(coupon_id);
        if (optCoupon.isPresent()) {
            return optCoupon.get();
        }
        throw new NoSuchCouponException("Coupon is not exist!");
    }

    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> allCoupons = couponRepo.findAll();
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon: allCoupons) {
            if (coupon.getAmount() > 0) {
                coupons.add(coupon); /*Lets hide all sold out coupons*/
            }
        }
        return coupons;
    }

    @Override
    public List<Coupon> getAllCustomerCoupons() {
        return couponRepo.findAllByCustomerId(customer_id);
    }

    @Override
    public List<Coupon> getCouponsByCompanyName(String name) {
        return couponRepo.findAllByCompanyName(name);
    }

    @Override
    public List<Coupon> getCouponsByCategory(int category) {
        return couponRepo.findAllByCategory(category);
    }

    @Override
    public List<Coupon> getCouponsByTitle(String title) {
        return couponRepo.findAllByTitle(title);
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
    public List<Coupon> getCouponsInPriceRange(double fromPrice, double toPrice) {
        return couponRepo.findAllByPriceBetween(fromPrice, toPrice);
    }

    @Override
    public void deleteCustomerItSelf(long id) throws NoSuchCustomerException {
        if (!customerRepo.findById(id).isPresent()) {
            throw new NoSuchCustomerException(String.format("Can't delete customer with such id#%d", id));
        }
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


}
