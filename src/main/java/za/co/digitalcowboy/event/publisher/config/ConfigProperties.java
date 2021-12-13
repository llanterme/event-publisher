package za.co.digitalcowboy.event.publisher.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigProperties {

    @Value("{app.otpGateWayUrl}")
    private String mobileRechargeApiUserName;

    @Value("{app.paygeniusCreateUrl}")
    private String paygeniusCreateUrl;

    @Value("{app.paygeniusEftUrl}")
    private String paygeniusEftUrl;

    @Value("{app.paygeniusSecret}")
    private String paygeniusSecret;

    @Value("{app.paygeniusToken}")
    private String paygeniusToken;

}