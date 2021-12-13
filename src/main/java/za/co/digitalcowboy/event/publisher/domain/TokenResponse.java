package za.co.digitalcowboy.event.publisher.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
    private String id_token;
}
