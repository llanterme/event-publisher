package za.co.digitalcowboy.event.publisher.service.eligibility;


import lombok.extern.slf4j.Slf4j;
import za.co.digitalcowboy.event.publisher.enums.EligibilityReasonEnum;
import za.co.digitalcowboy.event.publisher.response.EligibilityResponse;
import za.co.digitalcowboy.event.publisher.service.PropertyBag;

import java.util.function.Function;


@Slf4j
public class DuplicateUserEligibilityCheck {

    public static Function<PropertyBag, EligibilityResponse> executeDuplicateUserEligibilityCheck(){


        return (propertyBag) -> {
            EligibilityResponse eligibilityResponse = new EligibilityResponse();
            try {

                 eligibilityResponse = new EligibilityResponse();
                boolean userAlreadyRegistered = propertyBag.getUserRepository().validateUserAlreadyRegistered(propertyBag.getUser().getEmailAddress());
                if(!userAlreadyRegistered) {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
            } else  {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.USER_ALREADY_REGISTERED);
            }
                return eligibilityResponse;
            }catch (Exception e) {


                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.UNHANDLED_ELIGIBILITY_EXCEPTION);
                return eligibilityResponse;
            }

        };

    }



}
