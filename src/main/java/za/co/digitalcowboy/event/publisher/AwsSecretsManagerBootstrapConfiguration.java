package za.co.digitalcowboy.event.publisher;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.secretsmanager.AwsSecretsManagerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AwsSecretsManagerProperties.class)
@ConditionalOnClass({ AWSSecretsManager.class })
@ConditionalOnProperty(prefix = AwsSecretsManagerProperties.CONFIG_PREFIX, name = "enable", matchIfMissing = true)
public class AwsSecretsManagerBootstrapConfiguration {

//    public static final String REGION ="us-east-2";
//
//    @Bean
//    @ConditionalOnMissingBean
//    AWSSecretsManager smClient() {
//        return AWSSecretsManagerClientBuilder.standard().withRegion(REGION).build();
//    }
}
