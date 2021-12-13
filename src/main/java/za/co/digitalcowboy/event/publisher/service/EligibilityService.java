package za.co.digitalcowboy.event.publisher.service;

import za.co.digitalcowboy.event.publisher.response.EligibilityResponse;

public interface  EligibilityService {

     EligibilityResponse evaluateUserRegistrationEligibility(final PropertyBag propertyBag);

     EligibilityResponse evaluateDuplicateCouponEligibility(final PropertyBag propertyBag);

     EligibilityResponse userAuthenticationEligibilityCheck(final PropertyBag propertyBag);



}
