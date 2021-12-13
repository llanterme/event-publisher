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
public class CouponHistory extends BaseResponse {

    private int couponHistoryId;
    private int couponId;
    private int userId;
    private String dateAdded;

}
