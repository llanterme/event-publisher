package za.co.digitalcowboy.event.publisher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.digitalcowboy.event.publisher.response.BaseResponse;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class User extends BaseResponse implements Serializable {

    private int userId;
    private String emailAddress;
    private String name;
    private String dateRegistered;
    private String socialNetwork;
    private String deviceInfo;
    private String token;
}
