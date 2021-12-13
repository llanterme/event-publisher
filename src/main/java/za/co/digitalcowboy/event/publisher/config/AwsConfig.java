package za.co.digitalcowboy.event.publisher.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSns
@EnableSqs
public class AwsConfig {


    private AWSCredentials credentials;
    public AwsConfig() {

        this.credentials = new BasicAWSCredentials(
                "AKIAZHKB2QCTGTSLNYLA",
                "dru10F8LOYHCqH2CP910ylcc+eBtB3tCERv5+FWP"
        );
    }


    @Getter
    private AmazonSNS amazonSNSClient;


    @Getter
    private AmazonSQSAsync amazonSQSAsyncClient;

    @Setter
    @Value("${cloud.aws.region.static:us-east-2}")
    private String region;



    @Bean
    public void setAmazonSNSClient() {
        amazonSNSClient = AmazonSNSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    @Bean
    public void setAmazonSQSAsyncClient() {
        amazonSQSAsyncClient = AmazonSQSAsyncClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))

                .build();
    }


}
