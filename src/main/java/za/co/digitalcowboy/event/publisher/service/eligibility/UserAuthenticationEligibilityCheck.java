package za.co.digitalcowboy.event.publisher.service.eligibility;


import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import za.co.digitalcowboy.event.publisher.domain.User;
import za.co.digitalcowboy.event.publisher.entity.UserEntity;
import za.co.digitalcowboy.event.publisher.enums.EligibilityReasonEnum;
import za.co.digitalcowboy.event.publisher.response.EligibilityResponse;
import za.co.digitalcowboy.event.publisher.service.PropertyBag;

import java.util.function.Function;

/*
This check ensures the requesting product is active (1) in the DB and allowed to generate a token for.
*/

@Slf4j
public class UserAuthenticationEligibilityCheck {

    public static Function<PropertyBag, EligibilityResponse> executeUserAuthenticationEligibilityCheck(){

        return (propertyBag) -> {

            try {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();

                UserEntity userEntity = propertyBag.getUserRepository().authenticateUser(propertyBag.getUser().getEmailAddress());

            if(userEntity != null) {

                User authUser = new DozerBeanMapper().map(userEntity, User.class);

                propertyBag.setUser(authUser);
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
                }
             else {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.INVALID_USER_CREDENTIALS);
            }
                return eligibilityResponse;
            }catch (Exception e) {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.UNHANDLED_ELIGIBILITY_EXCEPTION);
                return eligibilityResponse;
            }

        };

    }



}
