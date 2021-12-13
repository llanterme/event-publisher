package za.co.digitalcowboy.event.publisher.service.eligibility;


import lombok.extern.slf4j.Slf4j;
import za.co.digitalcowboy.event.publisher.entity.CouponHistoryEntity;
import za.co.digitalcowboy.event.publisher.enums.EligibilityReasonEnum;
import za.co.digitalcowboy.event.publisher.response.EligibilityResponse;
import za.co.digitalcowboy.event.publisher.service.PropertyBag;

import java.util.function.Function;

@Slf4j
public class DuplicateCouponEligibilityCheck {

    public static Function<PropertyBag, EligibilityResponse> executeDuplicateCouponEligibilityCheck(){


        return (propertyBag) -> {
            EligibilityResponse eligibilityResponse = new EligibilityResponse();
            try {

                 eligibilityResponse = new EligibilityResponse();
                CouponHistoryEntity couponAlreadyCaptured = propertyBag.getCouponHistoryRepository().findByCouponId(propertyBag.getCouponHistory().getCouponId());
                if(couponAlreadyCaptured == null) {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
            } else  {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.COUPON_ALREADY_CAPTURED);
            }
                return eligibilityResponse;
            }catch (Exception e) {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.UNHANDLED_ELIGIBILITY_EXCEPTION);
                return eligibilityResponse;
            }

        };

    }



}
