package za.co.digitalcowboy.event.publisher.response;



import lombok.Getter;
import lombok.Setter;
import za.co.digitalcowboy.event.publisher.enums.EligibilityReasonEnum;


@Getter
@Setter
public class EligibilityResponse {


    private EligibilityReasonEnum eligibilityReason;


}
