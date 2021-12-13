package za.co.digitalcowboy.event.publisher.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.digitalcowboy.event.publisher.config.ConfigProperties;
import za.co.digitalcowboy.event.publisher.domain.User;
import za.co.digitalcowboy.event.publisher.entity.UserEntity;
import za.co.digitalcowboy.event.publisher.enums.EligibilityReasonEnum;
import za.co.digitalcowboy.event.publisher.enums.ServiceResponseCodesEnum;
import za.co.digitalcowboy.event.publisher.exceptions.EligibilityException;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;
import za.co.digitalcowboy.event.publisher.repository.CouponHistoryRepository;
import za.co.digitalcowboy.event.publisher.repository.UserRepository;
import za.co.digitalcowboy.event.publisher.response.EligibilityResponse;
import za.co.digitalcowboy.event.publisher.service.EligibilityService;
import za.co.digitalcowboy.event.publisher.service.PropertyBag;
import za.co.digitalcowboy.event.publisher.service.UserService;
import za.co.digitalcowboy.event.publisher.utils.DozerHelper;
import za.co.digitalcowboy.event.publisher.utils.LoggerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static za.co.digitalcowboy.event.publisher.utils.DozerHelper.toList;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private EligibilityService eligibilityService;

    private CouponHistoryRepository couponHistoryRepository;

    private PropertyBag propertyBag;

    private ConfigProperties configProperties;

//    private Sender producer;

    @Autowired
    public UserServiceImpl(EligibilityService eligibilityService, UserRepository userRepository, ConfigProperties configProperties,CouponHistoryRepository couponHistoryRepository
                           ) {
        this.eligibilityService = eligibilityService;
        this.userRepository = userRepository;
        this.configProperties = configProperties;


        propertyBag = new PropertyBag(userRepository,couponHistoryRepository);

    }

    @Override
    public User registerUser(User user) throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        propertyBag.setUser(user);

        log.info(loggerUtil.start("register user request for ", user));

        EligibilityResponse eligibilityResponse = eligibilityService.evaluateUserRegistrationEligibility(propertyBag);
        {

            if (eligibilityResponse.getEligibilityReason().equals(EligibilityReasonEnum.SUCCESSFUL)) {

                try {
                    user.setDateRegistered(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    UserEntity newUser = persistUser(user);
                    log.info(loggerUtil.end("register user request for ", user));
             //       producer.masterUserUpdated(user);
                    return new DozerBeanMapper().map(newUser, User.class);

                } catch (ServiceException e) {
                    throw new ServiceException(e.getResponseCodesEnum());
                }



            }
            else if (eligibilityResponse.getEligibilityReason().equals(EligibilityReasonEnum.USER_ALREADY_REGISTERED)) {
                return user;
            }
             else {
                throw new EligibilityException(eligibilityResponse.getEligibilityReason().getReasonCode(), eligibilityResponse.getEligibilityReason().getResponseStatus());
            }

        }

    }

    @Override
    public User findUser(String emailAddress) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();

        log.info(loggerUtil.start("finder user for ", emailAddress));
        try {
            return new DozerBeanMapper().map(userRepository.findUserByEmailAddress(emailAddress), User.class);
        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to find user in database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_FIND_USER);
        }

    }

    @Override
    public List<User> getAllUsers() throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<User> userList;

        try {
            log.info(loggerUtil.start("get user request", null));
            userList = DozerHelper.map(new DozerBeanMapper(), toList(userRepository.findAll()), User.class);
            log.info(loggerUtil.end("get coupons request", null));
            return userList;

        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to get a list of users from the database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_GET_USERS_LIST);

        }
    }

    @Override
    public User authUser(User user) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        propertyBag.setUser(user);

        log.info(loggerUtil.start("auth user request for ", user));

        EligibilityResponse eligibilityResponse = eligibilityService.userAuthenticationEligibilityCheck(propertyBag);
        {

            if (eligibilityResponse.getEligibilityReason().equals(EligibilityReasonEnum.SUCCESSFUL)) {

                log.info(loggerUtil.end("auth user request for ", user));
                return propertyBag.getUser();

            } else {
                throw new EligibilityException(eligibilityResponse.getEligibilityReason().getReasonCode(), eligibilityResponse.getEligibilityReason().getResponseStatus());
            }

        }
    }

//    @Override
//    public void recover(ServiceException e, User user) {
//        System.out.println(e.getResponseCodesEnum());
//    }

    private UserEntity persistUser(User user) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        try {
            UserEntity newUser = userRepository.save(new DozerBeanMapper().map(user, UserEntity.class));
            return newUser;
        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to save user to database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_REGISTER_USER);
        }

    }

}
