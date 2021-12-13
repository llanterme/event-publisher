package za.co.digitalcowboy.event.publisher.controller;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.digitalcowboy.event.publisher.config.AwsConfig;
import za.co.digitalcowboy.event.publisher.domain.*;
import za.co.digitalcowboy.event.publisher.domain.payment.PayGeniusRequest;
import za.co.digitalcowboy.event.publisher.domain.payment.PayGeniusResponse;
import za.co.digitalcowboy.event.publisher.enums.ServiceResponseCodesEnum;
import za.co.digitalcowboy.event.publisher.exceptions.ApiAuthenticationException;
import za.co.digitalcowboy.event.publisher.exceptions.EligibilityException;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;
import za.co.digitalcowboy.event.publisher.provider.ApiAuthenticationProvider;
import za.co.digitalcowboy.event.publisher.response.BaseResponse;
import za.co.digitalcowboy.event.publisher.service.*;
import za.co.digitalcowboy.event.publisher.utils.LoggerUtil;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static za.co.digitalcowboy.event.publisher.utils.Utils.getObjectMapper;





@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {

    private ApiAuthenticationProvider apiAuthenticationProvider;
    private UserService userService;
    private CouponService couponService;
    private IndustryService industryService;
    private BluffService bluffService;
//    private  Sender sender;
    private PaymentService paymentService;

    private AwsConfig awsConfig;


    private static final String CITY_QUEUE = "sqs-city-queue";

    @Autowired
    public RestController(ApiAuthenticationProvider apiAuthenticationProvider, UserService userService,CouponService couponService,IndustryService industryService,
                          BluffService bluffService, PaymentService paymentService,AwsConfig awsConfig) {
        this.apiAuthenticationProvider = apiAuthenticationProvider;
        this.userService = userService;
        this.couponService = couponService;
        this.industryService = industryService;
        this.bluffService = bluffService;
        this.paymentService = paymentService;
        this.awsConfig = awsConfig;


   }


    @RequestMapping(path = "/health", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getHealthz() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }



    @GetMapping(value = "/sendSqsRegisterMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> sendSqsMessage() throws Exception {

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("name", new MessageAttributeValue()
                .withStringValue("llanterme")
                .withDataType("String"));

        messageAttributes.put("emailAddress", new MessageAttributeValue()
                .withStringValue("llanterme@gmail.com")
                .withDataType("String"));


        SendMessageRequest sendMessageStandardQueue = new SendMessageRequest()
                .withQueueUrl(CITY_QUEUE)
                .withMessageBody("registration message")
                .withMessageAttributes(messageAttributes);

        awsConfig.getAmazonSQSAsyncClient().sendMessage(sendMessageStandardQueue);


        return ResponseEntity.status(HttpStatus.OK).body("Message Sent");
    }


    @RequestMapping(value = "/getCoupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCoupons(@RequestHeader(value="Authorization") String authorization) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Get coupons", null));
        List<Coupon> couponList;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            couponList = couponService.getCoupons();
            log.info(loggerUtil.end("milestone: Get coupons", couponList));
            return ResponseEntity.status(HttpStatus.OK).body(couponList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting coupons:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting couponss:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/filterCoupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity filterCoupons(@RequestHeader(value="Authorization") String authorization, String industryId) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Get coupons", null));
        List<Coupon> couponList;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            couponList = couponService.filterCoupons(Integer.parseInt(industryId));
            log.info(loggerUtil.end("milestone: Filter coupons", couponList));
            return ResponseEntity.status(HttpStatus.OK).body(couponList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while filtering coupons:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error filtering coupons:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/findUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findUser(@RequestHeader(value="Authorization") String authorization, String emailAddress) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Find user", null));
        User user;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            user = userService.findUser(emailAddress);
            log.info(loggerUtil.end("milestone: Found user", user));
            return ResponseEntity.status(HttpStatus.OK).body(user);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while filtering coupons:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error filtering coupons:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/userBluffHistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserBluffHistory(@RequestHeader(value="Authorization") String authorization, String userId) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Get bluff history", null));
        List<BluffHistory> bluffHistoryList;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            bluffHistoryList = bluffService.getUserBluffs(Integer.parseInt(userId));
            log.info(loggerUtil.end("milestone: Bluff history", bluffHistoryList));
            return ResponseEntity.status(HttpStatus.OK).body(bluffHistoryList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting bluff history:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error while getting bluff history:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }


    @RequestMapping(value = "/industries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getIndustries(@RequestHeader(value="Authorization") String authorization) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Get coupons", null));
        List<Industry> industryList;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            industryList = industryService.getIndustries();
            log.info(loggerUtil.end("milestone: Get industries", industryList));
            return ResponseEntity.status(HttpStatus.OK).body(industryList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting industries:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting industries:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUusers(@RequestHeader(value="Authorization") String authorization) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Get users", null));
        List<User> userList;
        String secretJson = getSecret();

        ObjectMapper mapper = new ObjectMapper();

        try {

            Secrets lib = mapper.readValue(secretJson, Secrets.class);
            System.out.println(lib.getApikey());

            apiAuthenticationProvider.authenticateApi(authorization);
            userList = userService.getAllUsers();
            log.info(loggerUtil.end("milestone: Get Users", userList));
            return ResponseEntity.status(HttpStatus.OK).body(userList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting industries:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting users list:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());

    } catch (JsonProcessingException serviceException) {
        log.error(loggerUtil.error("milestone: mapping exception error getting users list:", serviceException.getMessage()));
        return buildServiceExceptionResponse(ServiceResponseCodesEnum.UNABLE_TO_FIND_USER);
    }

    }

    @PostMapping(value = "/authUser")
    public ResponseEntity authUer(@RequestHeader(value="Authorization") String authorization, @RequestBody User user) {

        LoggerUtil loggerUtil = new LoggerUtil();
        User authedUser;
        log.info(loggerUtil.start("auth user request inbound payload:", user));

        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            authedUser = userService.authUser(user);
            log.info(loggerUtil.end("milestone: auth user response", authedUser));

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(authedUser);
        } catch (EligibilityException knoxEligibilityException) {
            log.error(loggerUtil.error("milestone: Eligibility error while auth user:", knoxEligibilityException.getErrorMessage()));
            return buildEligibilityResponseExceptionResponse(knoxEligibilityException);
        }  catch (ApiAuthenticationException apiAuthenticationException) {

            log.error(loggerUtil.error("milestone: API Authentication error while auth user:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error while auth user:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }


    @PostMapping(value = "/addCouponHistory")
    public ResponseEntity addCouponHistory(@RequestHeader(value="Authorization") String authorization, @RequestBody CouponHistory couponHistory) {

        LoggerUtil loggerUtil = new LoggerUtil();
        CouponHistory addedCoupon;
        log.info(loggerUtil.start("coupon history request inbound payload:", couponHistory));

        try {
            apiAuthenticationProvider.authenticateApi(authorization);
                addedCoupon = couponService.addCouponHistory(couponHistory);
            log.info(loggerUtil.end("milestone: coupon history response", addedCoupon));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(addedCoupon);
        } catch (EligibilityException knoxEligibilityException) {
            log.error(loggerUtil.error("milestone: Eligibility error while registering coupon history:", knoxEligibilityException.getErrorMessage()));
            return buildEligibilityResponseExceptionResponse(knoxEligibilityException);
        }  catch (ApiAuthenticationException apiAuthenticationException) {

            log.error(loggerUtil.error("milestone: API Authentication error while registering coupon history:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error while registering coupon history:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @PostMapping(value = "/addBluffHistory")
    public ResponseEntity addBluffHistory(@RequestHeader(value="Authorization") String authorization, @RequestBody BluffHistory bluffHistory) {

        LoggerUtil loggerUtil = new LoggerUtil();
        BluffHistory addBluffHistory;
        log.info(loggerUtil.start("bluff history request inbound payload:", bluffHistory));

        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            addBluffHistory = bluffService.addBluffHistory(bluffHistory);
            log.info(loggerUtil.end("milestone: bluff history response", addBluffHistory));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(addBluffHistory);
        }  catch (ApiAuthenticationException apiAuthenticationException) {
           log.error(loggerUtil.error("milestone: API Authentication error while adding bluff history:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error while adding bluff history:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }


    @PostMapping(value = "/registerUser")
    public ResponseEntity registerUser(@RequestHeader(value="Authorization") String authorization, @RequestBody User user) {

        LoggerUtil loggerUtil = new LoggerUtil();
        User userRegistrationResponse;
        log.info(loggerUtil.start("register user request inbound payload:", user));

        try {
        //    apiAuthenticationProvider.authenticateApi(authorization);
            userRegistrationResponse = userService.registerUser(user);
            log.info(loggerUtil.end("milestone: register user response", userRegistrationResponse));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userRegistrationResponse);
        } catch (EligibilityException knoxEligibilityException) {
            log.error(loggerUtil.error("milestone: Eligibility error while registering user:", knoxEligibilityException.getErrorMessage()));
            return buildEligibilityResponseExceptionResponse(knoxEligibilityException);
        }  catch (ApiAuthenticationException apiAuthenticationException) {

            log.error(loggerUtil.error("milestone: API Authentication error while registering user:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error while registering user:", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }


    @PostMapping(value = "/createPaymentEndPoint")
    public ResponseEntity createPaymentEndPoint(@RequestHeader(value="Authorization") String authorization, @RequestBody PayGeniusRequest payGeniusRequest) {

        LoggerUtil loggerUtil = new LoggerUtil();
        PayGeniusResponse payGeniusResponse;
        log.info(loggerUtil.start("create payment end point inbound payload", payGeniusRequest));

        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            payGeniusResponse = paymentService.createPayEndPoint();
            log.info(loggerUtil.end("milestone: create payment end point response", payGeniusResponse));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(payGeniusResponse);
        }  catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while creating payment end point:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error while creating payment end point: :", serviceException.getResponseCodesEnum()));
            return buildServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/sendDecline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendDecline() {

        try {

            ObjectMapper objectMapper = getObjectMapper();

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            TransactionDeclinedDto transactionDeclinedDto = objectMapper.readValue(classloader.getResourceAsStream("declines.json"), TransactionDeclinedDto.class);

          //  sender.sendDeclinedTransaction(transactionDeclinedDto);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            System.out.println("Could not add person " + e.getMessage());
        }

        return null;

    }


    @PostMapping(value = "/sendMDS")
    public ResponseEntity sendMDS(@RequestBody User user) {

        try {

         //   sender.masterUserUpdated(user);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            System.out.println("Could not add person " + e.getMessage());
        }

        return null;

    }






    private ResponseEntity<?> buildApiAuthenticationExceptionResponse() {
        BaseResponse errorResponse = new BaseResponse();
        errorResponse.setResponseMessage(ServiceResponseCodesEnum.API_AUTHENTICATION_ERROR.getMessage());
        errorResponse.setResponseCode(ServiceResponseCodesEnum.API_AUTHENTICATION_ERROR.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ResponseEntity<?> buildServiceExceptionResponse(ServiceResponseCodesEnum serviceResponseCodesEnum) {
        BaseResponse errorResponse = new BaseResponse();
        errorResponse.setResponseMessage(serviceResponseCodesEnum.getMessage());
        errorResponse.setResponseCode(serviceResponseCodesEnum.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ResponseEntity<?> buildEligibilityResponseExceptionResponse(EligibilityException eligibilityException) {
        BaseResponse errorResponse = new BaseResponse();
        errorResponse.setResponseMessage(eligibilityException.getErrorMessage());
        errorResponse.setResponseCode(Integer.parseInt(eligibilityException.getErrorCode()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private String getSecret() {
        String secretName = "dev/grapevine/api";
        String region = "us-east-2";

        String secret = null;
        AWSSecretsManager awsSecretsManager = AWSSecretsManagerClientBuilder.standard().withRegion(region).build();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        getSecretValueResult = awsSecretsManager.getSecretValue(getSecretValueRequest);

        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
        } else {
            secret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }
        return secret;
    }
}
