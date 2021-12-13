package za.co.digitalcowboy.event.publisher.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import za.co.digitalcowboy.event.publisher.config.ConfigProperties;
import za.co.digitalcowboy.event.publisher.domain.payment.*;
import za.co.digitalcowboy.event.publisher.enums.ServiceResponseCodesEnum;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;
import za.co.digitalcowboy.event.publisher.service.PaymentService;
import za.co.digitalcowboy.event.publisher.utils.LoggerUtil;
import za.co.digitalcowboy.event.publisher.utils.RestUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

import static za.co.digitalcowboy.event.publisher.utils.RestUtil.configureRestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private ConfigProperties configProperties;

    final Gson gson;

    final String createUrl;


    @Autowired
    public PaymentServiceImpl (ConfigProperties configProperties) {

            this.configProperties = configProperties;
            gson = new GsonBuilder().setPrettyPrinting().create();
            createUrl = configProperties.getPaygeniusCreateUrl();
    }



    @Override
    public PayGeniusResponse createPayEndPoint() throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();

        PayGeniusRequest payload = createDummyPayment();
        loggerUtil.info(loggerUtil.start("created dummy payment payload", payload));

        String signature = generateSecureRequestSignature(payload);
        loggerUtil.info(loggerUtil.info("created secure signature", signature));

        String flatPayload = gson.toJson(payload);
        flatPayload= flatPayload.replace("\n", "").replace(" ", "");

        loggerUtil.info(loggerUtil.info("created dummy flat payment payload", flatPayload));


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ResponseEntity<String> responseEntity;
            try {

                responseEntity = configureRestTemplate().exchange(createUrl, HttpMethod.POST, new HttpEntity<Object>(flatPayload, RestUtil.getHeaders(signature)), String.class);

                if (RestUtil.isError(responseEntity.getStatusCode())) {

                    PayGeniusErrorResponse payGeniusErrorResponse = objectMapper.readValue(responseEntity.getBody(), PayGeniusErrorResponse.class);

                    loggerUtil.error(loggerUtil.error("error creating payment end point: ", payGeniusErrorResponse.getError()));
                    throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_CREATE_PAYMENT_ENDPOINT);

                }

                PayGeniusResponse payGeniusResponse = objectMapper.readValue(responseEntity.getBody(), PayGeniusResponse.class);

                return payGeniusResponse;

            } catch (HttpStatusCodeException e) {
                loggerUtil.error(loggerUtil.error("error creating payment end point: ", e.getMessage()));
                throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_CREATE_PAYMENT_ENDPOINT);
            }

        } catch (Exception e) {
            loggerUtil.error(loggerUtil.error("error creating payment end point: ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_CREATE_PAYMENT_ENDPOINT);
        }

    }

    private String generateSecureRequestSignature(PayGeniusRequest payGeniusRequest) throws ServiceException {

        String jsonSigningPayload = gson.toJson(payGeniusRequest);
        jsonSigningPayload= jsonSigningPayload.replace("\n", "").replace(" ", "");

        try {

            String payload = jsonSigningPayload;
            String secret = configProperties.getPaygeniusSecret();

            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(Charset.defaultCharset()), "HmacSHA256");
            hmac.init(secretKeySpec);

            String signature = new String(Hex.encodeHex(hmac.doFinal(String.format("%s\n%s", createUrl, payload).trim().getBytes(Charset.defaultCharset()))));

            return signature;
        } catch (Exception e) {
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_GENERATE_SECURE_SIGNATURE);
        }


    }

    private PayGeniusRequest createDummyPayment() {
        Transaction transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setReference("Luke Test Reference");
        transaction.setCurrency("ZAR");

        Address address = new Address();
        address.setAddressLineOne("2 Boca Raton, Woodburn Road");
        address.setCity("Johannesburg");
        address.setPostCode("2196");
        address.setCountry("South Africa");

        Consumer consumer = new Consumer();
        consumer.setAddress(address);
        consumer.setEmail("llanterme@gmail.com");
        consumer.setSurname("Lanterme");
        consumer.setName("Luke");

        Urls urls = new Urls();
        urls.setCancel("http://www.cancelurl.com");
        urls.setSuccess("http://www.successurl.com");
        urls.setError("http://www.errorurl.com");


        PayGeniusRequest payGeniusRequest = new PayGeniusRequest();
        payGeniusRequest.setTransaction(transaction);
        payGeniusRequest.setConsumer(consumer);
        payGeniusRequest.setUrls(urls);
        payGeniusRequest.setAuth(false);
        payGeniusRequest.setPageUrlKey("demo_page");


        return payGeniusRequest;

    }




}
