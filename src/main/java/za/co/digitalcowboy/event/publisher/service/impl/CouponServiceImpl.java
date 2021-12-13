package za.co.digitalcowboy.event.publisher.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.digitalcowboy.event.publisher.domain.Coupon;
import za.co.digitalcowboy.event.publisher.domain.CouponHistory;
import za.co.digitalcowboy.event.publisher.entity.CouponHistoryEntity;
import za.co.digitalcowboy.event.publisher.enums.EligibilityReasonEnum;
import za.co.digitalcowboy.event.publisher.enums.ServiceResponseCodesEnum;
import za.co.digitalcowboy.event.publisher.exceptions.EligibilityException;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;
import za.co.digitalcowboy.event.publisher.repository.CouponHistoryRepository;
import za.co.digitalcowboy.event.publisher.repository.CouponRepository;
import za.co.digitalcowboy.event.publisher.repository.IndustryRepository;
import za.co.digitalcowboy.event.publisher.repository.UserRepository;
import za.co.digitalcowboy.event.publisher.response.EligibilityResponse;
import za.co.digitalcowboy.event.publisher.service.CouponService;
import za.co.digitalcowboy.event.publisher.service.EligibilityService;
import za.co.digitalcowboy.event.publisher.service.PropertyBag;
import za.co.digitalcowboy.event.publisher.utils.DozerHelper;
import za.co.digitalcowboy.event.publisher.utils.LoggerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static za.co.digitalcowboy.event.publisher.utils.DozerHelper.toList;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    private CouponRepository couponRepository;
    private CouponHistoryRepository couponHistoryRepository;
    private IndustryRepository industryRepository;
    private UserRepository userRepository;
    private EligibilityService eligibilityService;
    private PropertyBag propertyBag;

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository, IndustryRepository industryRepository, CouponHistoryRepository couponHistoryRepository,
                             UserRepository userRepository, EligibilityService eligibilityService) {
        this.couponRepository = couponRepository;
        this.industryRepository = industryRepository;
        this.couponHistoryRepository = couponHistoryRepository;
        this.userRepository = userRepository;
        this.eligibilityService = eligibilityService;
        propertyBag = new PropertyBag(userRepository, couponHistoryRepository);
    }

    @Override
    public List<Coupon> getCoupons() throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<Coupon> couponList;

        try {
            log.info(loggerUtil.start("get coupons request", null));
            couponList = DozerHelper.map(new DozerBeanMapper(), toList(couponRepository.findAll()), Coupon.class);
            couponList.forEach(c -> c.setIndustry(industryRepository.getIndustryEntitiesByIndustryId(c.getIndustryId()).getIndustry()));
            log.info(loggerUtil.end("get coupons request", null));

            return couponList;

        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to get coupons from the database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_GET_COUPONS);

        }
    }

    @Override
    public List<Coupon> filterCoupons(int industryId) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<Coupon> couponList;

        try {
            log.info(loggerUtil.start("filter coupons request for id", industryId));
            couponList = DozerHelper.map(new DozerBeanMapper(), toList(couponRepository.getCouponEntitiesByIndustryId(industryId)), Coupon.class);
            couponList.forEach(c -> c.setIndustry(industryRepository.getIndustryEntitiesByIndustryId(c.getIndustryId()).getIndustry()));
            log.info(loggerUtil.end("filter coupons request for id", industryId));

            return couponList;

        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to get filter coupons from the database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_ADD_FILTER_COUPONS);

        }

    }

    @Override
    public CouponHistory addCouponHistory(CouponHistory couponHistory) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        propertyBag.setCouponHistory(couponHistory);
        EligibilityResponse eligibilityResponse = eligibilityService.evaluateDuplicateCouponEligibility(propertyBag);
        {

            if (eligibilityResponse.getEligibilityReason().equals(EligibilityReasonEnum.SUCCESSFUL)) {

                try {

                    log.info(loggerUtil.start("add coupon history ", couponHistory));
                    couponHistory.setDateAdded(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    CouponHistoryEntity newCouponHistory = couponHistoryRepository.save(new DozerBeanMapper().map(couponHistory, CouponHistoryEntity.class));
                    couponHistory.setCouponHistoryId(newCouponHistory.getCouponHistoryId());
                    log.info(loggerUtil.end("add coupon history ", couponHistory));
                    return couponHistory;
                } catch (DataAccessException e) {
                    log.error(loggerUtil.end("unable to add coupon history to database ", e.getMessage()));
                    throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_ADD_COUPON_HISTORY);
                }
            } else {
                throw new EligibilityException(eligibilityResponse.getEligibilityReason().getReasonCode(), eligibilityResponse.getEligibilityReason().getResponseStatus());
            }
        }
    }
}
