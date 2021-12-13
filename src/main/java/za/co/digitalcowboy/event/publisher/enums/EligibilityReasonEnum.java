package za.co.digitalcowboy.event.publisher.enums;

import java.io.Serializable;
import java.util.Arrays;

public enum EligibilityReasonEnum implements Serializable {


    UNHANDLED_ELIGIBILITY_EXCEPTION("999", "Unhandled Exception on eligibility evaluation", 1),
    USER_ALREADY_REGISTERED("02", "User already registered", 100),
    COUPON_ALREADY_CAPTURED("03", "Coupon already captured", 101),
    INVALID_USER_CREDENTIALS("03", "Invalid username or password", 102),
    SUCCESSFUL("00", "Successful", 999);




    private final String reasonCode;
    private final String responseStatus;
    private final int priority;

    public static EligibilityReasonEnum findEligibilityReason(String reasonCode) {
        EligibilityReasonEnum result = (EligibilityReasonEnum) Arrays.stream(values()).filter((eligibilityReason) -> {
            return eligibilityReason.getReasonCode().equalsIgnoreCase(reasonCode) || eligibilityReason.getReasonCode().equalsIgnoreCase("0" + reasonCode);
        }).findFirst().orElse((null));
        return result;
    }

    public String getReasonCode() {
        return this.reasonCode;
    }

    public String getResponseStatus() {
        return this.responseStatus;
    }

    public int getPriority() {
        return this.priority;
    }

    private EligibilityReasonEnum(String reasonCode, String responseStatus, int priority) {
        this.reasonCode = reasonCode;
        this.responseStatus = responseStatus;
        this.priority = priority;
    }



}
