package za.co.digitalcowboy.event.publisher.service;

import za.co.digitalcowboy.event.publisher.domain.Coupon;
import za.co.digitalcowboy.event.publisher.domain.CouponHistory;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;

import java.util.List;

public interface CouponService {

    List<Coupon> getCoupons() throws ServiceException;

    List<Coupon> filterCoupons(int industryId) throws ServiceException;

    CouponHistory addCouponHistory(CouponHistory couponHistory) throws ServiceException;


}
