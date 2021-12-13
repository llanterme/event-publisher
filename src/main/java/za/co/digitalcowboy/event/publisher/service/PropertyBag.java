package za.co.digitalcowboy.event.publisher.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.digitalcowboy.event.publisher.config.ConfigProperties;
import za.co.digitalcowboy.event.publisher.domain.CouponHistory;
import za.co.digitalcowboy.event.publisher.domain.User;
import za.co.digitalcowboy.event.publisher.repository.CouponHistoryRepository;
import za.co.digitalcowboy.event.publisher.repository.UserRepository;


@Slf4j
public class PropertyBag {


    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    CouponHistory couponHistory;

    @Getter
    private ConfigProperties configProperties;

    @Getter
    @Setter
    private UserRepository userRepository;

    @Getter
    @Setter
    CouponHistoryRepository couponHistoryRepository;


    @Autowired
    public PropertyBag(UserRepository userRepository, CouponHistoryRepository couponHistoryRepository) {
        this.userRepository = userRepository;
        ConfigProperties configProperties = new ConfigProperties();
        this.configProperties = configProperties;
        this.couponHistoryRepository = couponHistoryRepository;

    }





}