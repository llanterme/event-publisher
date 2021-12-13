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
public class BluffHistory extends BaseResponse {

    private int bluffHistoryId;
    private int userId;
    private String angryScore;
    private String joyScore;
    private String sorrowScore;
    private String surprisedScore;
    private String dateAdded;
    private String totalWeighting;
    private String imageUrl;
}
