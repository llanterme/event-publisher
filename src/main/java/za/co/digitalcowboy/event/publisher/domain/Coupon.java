package za.co.digitalcowboy.event.publisher.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.digitalcowboy.event.publisher.response.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends BaseResponse {

    private int couponId;
    private String organizationId;
    private int industryId;
    private String industry;
    private String coupon;
    private String description;
}
