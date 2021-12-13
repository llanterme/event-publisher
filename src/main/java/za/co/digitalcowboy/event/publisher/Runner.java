package za.co.digitalcowboy.event.publisher;

import com.prowidesoftware.swift.model.SwiftBlock3;
import com.prowidesoftware.swift.model.SwiftBlock4;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import com.prowidesoftware.swift.utils.Lib;

import java.nio.charset.StandardCharsets;

public class Runner {


    public static void main(String[] args) {


        try {

            MT103 mt = MT103.parse(Lib.readResource("mt103.txt", StandardCharsets.UTF_8.name()));
            SwiftMessage swiftMessage = mt.getSwiftMessage();

            swiftMessage = SwiftMessage.parse(swiftMessage.getUnparsedTexts().getAsFINString());

            SwiftBlock3 swiftBlock3 = swiftMessage.getBlock3();
            Tag tag4 = swiftBlock3.getTag(4);
            Field tag4Field = tag4.asField();
            System.out.println(tag4Field.getComponent(1));

            SwiftBlock4 swiftBlock4 = swiftMessage.getBlock4();
            Tag tag6 = swiftBlock4.getTag(6);
            Field field = tag6.asField();
            System.out.println(field.getComponent(1));
            System.out.println(field.getComponent(2));






        } catch (Exception e) {
            System.out.println(e.getMessage());
        }




//                try {
//
//            String endpoint = "https://developer.paygenius.co.za/pg/api/v2/create/eft";
//             String payload = "{\"transaction\":{ \"reference\":\"Invoice #1871\", \"currency\":\"ZAR\", \"amount\":100 }, \"consumer\": { \"name\": \"Bilbo\", \"surname\": \"Baggins\", \"email\": \"bilbo@shire.com\" }, \"urls\": { \"success\": \"https://merchant.com/success-page\", \"cancel\": \"https://merchant.com/cancel-page\", \"error\": \"https://merchant.com/error-page\"}}".trim();
//            //String payload = jsonSigningPayload;
//            String secret = "29b6c0d5-9169-4d54-989d-10c3dfc198c3";
//
//            Mac hmac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(Charset.defaultCharset()), "HmacSHA256");
//            hmac.init(secretKeySpec);
//
//            String signature = new String(Hex.encodeHex(hmac.doFinal(String.format("%s\n%s", endpoint, payload).trim().getBytes(Charset.defaultCharset()))));
//
//                    System.out.println(signature);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//
//    }


//
//    static PayGeniusResponse createPaymentEndPoint() throws KnoxServiceException {
//
//        String url = "https://developer.paygenius.co.za/pg/api/v2/redirect/create";
//        PayGeniusRequest payload = createDummyPayment();
//        String signature = generateSecureRequestSignature(payload);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String flatPayload = gson.toJson(payload);
//        flatPayload= flatPayload.replace("\n", "").replace(" ", "");
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            ResponseEntity<String> responseEntity = null;
//            try {
//
//            responseEntity = configureRestTemplate().exchange(url, HttpMethod.POST, new HttpEntity<Object>(flatPayload, RestUtil.getHeaders(signature)), String.class);
//
//                if (RestUtil.isError(responseEntity.getStatusCode())) {
//
//                PayGeniusErrorResponse payGeniusErrorResponse = objectMapper.readValue(responseEntity.getBody(), PayGeniusErrorResponse.class);
//                throw new KnoxServiceException(payGeniusErrorResponse.getError().getMessage());
//
//                }
//
//                PayGeniusResponse payGeniusResponse = objectMapper.readValue(responseEntity.getBody(), PayGeniusResponse.class);
//
//                return payGeniusResponse;
//
//            } catch (HttpStatusCodeException e) {
//
//                throw new KnoxServiceException(e.getMessage());
//            }
//
//        } catch (Exception e) {
//            throw new KnoxServiceException(e.getMessage());
//        }
//
//
//    }

//    static String generateSecureRequestSignature(PayGeniusRequest payGeniusRequest) {
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String jsonSigningPayload = gson.toJson(payGeniusRequest);
//        jsonSigningPayload= jsonSigningPayload.replace("\n", "").replace(" ", "");
//
//        try {
//
//            String endpoint = "https://developer.paygenius.co.za/pg/api/v2/redirect/create";
//            // String payload = "{\"transaction\":{\"reference\":\"Invoice #1871\",\"currency\":\"ZAR\",\"amount\":100},\"consumer\":{\"name\":\"Bilbo\",\"surname\":\"Baggins\",\"email\":\"bilbo@shire.com\",\"address\":{\"addressLineOne\":\"10 Test Avenue\",\"city\":\"Cape Town\",\"postCode\":\"7441\",\"country\":\"ZA\"}},\"urls\":{\"success\":\"https://merchant.com/success-page\",\"cancel\":\"https://merchant.com/cancel-page\",\"error\":\"https://merchant.com/error-page\"},\"auth\":false,\"pageUrlKey\":\"demo_page\"}";
//            String payload = jsonSigningPayload;
//            String secret = "29b6c0d5-9169-4d54-989d-10c3dfc198c3";
//
//            Mac hmac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(Charset.defaultCharset()), "HmacSHA256");
//            hmac.init(secretKeySpec);
//
//            String signature = new String(Hex.encodeHex(hmac.doFinal(String.format("%s\n%s", endpoint, payload).trim().getBytes(Charset.defaultCharset()))));
//
//            return signature;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//            return null;
//
//    }
//
//    static PayGeniusRequest createDummyPayment() {
//        Transaction transaction = new Transaction();
//        transaction.setAmount(1000);
//        transaction.setReference("Luke Test Reference");
//        transaction.setCurrency("ZAR");
//
//        Address address = new Address();
//        address.setAddressLineOne("2 Boca Raton, Woodburn Road");
//        address.setCity("Johannesburg");
//        address.setPostCode("2196");
//        address.setCountry("South Africa");
//
//        Consumer consumer = new Consumer();
//        consumer.setAddress(address);
//        consumer.setEmail("llanterme@gmail.com");
//        consumer.setSurname("Lanterme");
//        consumer.setName("Luke");
//
//        Urls urls = new Urls();
//        urls.setCancel("http://www.cancelurl.com");
//        urls.setSuccess("http://www.successurl.com");
//        urls.setError("http://www.errorurl.com");
//
//
//        PayGeniusRequest payGeniusRequest = new PayGeniusRequest();
//        payGeniusRequest.setTransaction(transaction);
//        payGeniusRequest.setConsumer(consumer);
//        payGeniusRequest.setUrls(urls);
//        payGeniusRequest.setAuth(false);
//        payGeniusRequest.setPageUrlKey("demo_page");
//
//
//        return payGeniusRequest;
//
//    }


//}

    }





}
