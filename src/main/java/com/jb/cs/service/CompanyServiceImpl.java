package com.jb.cs.service;

import com.jb.cs.data.db.CompanyRepository;
import com.jb.cs.data.db.CouponRepository;
import com.jb.cs.data.db.CustomerRepository;
import com.jb.cs.data.ex.NoSuchCompanyException;
import com.jb.cs.data.ex.NoSuchCouponException;
import com.jb.cs.data.model.Company;
import com.jb.cs.data.model.Coupon;
import com.jb.cs.data.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompanyServiceImpl implements CompanyService {

    private long company_id;
    private CouponRepository couponRepo;
    private CompanyRepository companyRepo;
    private ApplicationContext context;

    /*Set User in UserSystem*/
    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    @Autowired
    public CompanyServiceImpl(CouponRepository couponDao, CompanyRepository companyDao, ApplicationContext context) {
        this.couponRepo = couponDao;
        this.companyRepo = companyDao;
        this.context = context;
    }



    // GETTER'S FOR COMPANY
    @Override/*Returns company to it selves*/
    public Company getCompany(){
        Optional<Company> optCompany = companyRepo.findById(company_id);
        return optCompany.get();
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    @Override
    public Company getCompanyById(long id) {
        return companyRepo.findById(id).orElse(Company.empty());
    }




    // UPDATE FOR COMPANY
//    @Override
//    public Company updateCompany(Company company) throws NoSuchCompanyException {
//        Optional<Company> optionalCompany = companyRepo.findById(company.getId());
//        if (optionalCompany.isPresent()) {
//            Company existCompany = optionalCompany.get();
//            existCompany.setName(company.getName());
//            existCompany.setImageURL(company.getImageURL());
//            return companyRepo.save(existCompany);
//        }
//        throw new NoSuchCompanyException(String.format("There is no customer with this id:%d"));
//    }

    @Override
    public void updateCompany(Company company) throws NoSuchCompanyException {
        companyRepo.save(company);
    }


    // GETTER'S FOR COUPON
    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    @Override
    public List<Coupon> getAllCompanyCoupons(long id) {
        return couponRepo.findAllByCompanyId(id);
    }




    // UPDATE FOR COUPON
    @Override
    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        Optional<Company> optCompany = companyRepo.findById(company_id);
        if (!optCompany.isPresent()) {
            //Never happen
        }
        Company company = optCompany.get();

        if (company.getCoupons() != null) {
            for (Coupon existCoupon : company.getCoupons()) {
                if (coupon.similarCoupon(existCoupon)) {
                    existCoupon.setAmount(existCoupon.getAmount() + coupon.getAmount());
                    existCoupon.setCompanyId(company_id);
                    return couponRepo.save(existCoupon);
                }
            }
        }
        coupon.setCompanyId(company_id);
        company.add(coupon);
        return couponRepo.save(coupon);
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
    public void deleteCouponById(long coupon_id) throws NoSuchCouponException {
        Optional<Coupon> optCoupon = couponRepo.findById(coupon_id);
        if (!optCoupon.isPresent()) {
            throw new NoSuchCouponException(String.format("Coupon with id#%d does not exist.", coupon_id));
        }

        Coupon coupon = optCoupon.get();
        CustomerRepository customerRepo = context.getBean(CustomerRepository.class);

        for (Customer customer : customerRepo.findAll()) {
            if (customer.getCoupons().contains(coupon)) {
                customer.getCoupons().remove(coupon);
            }
        }

        Company company = companyRepo.findById(company_id).get();
        List<Coupon> coupons = company.getCoupons();
        coupons.remove(coupon);
        company.setCoupons(coupons);
        couponRepo.deleteById(coupon_id);
        System.out.println("Coupon was deleted successfully!");
    }

    @Override
    public void deleteCouponByCompanyId(long id) {
        couponRepo.deleteById(id);
    }


}
