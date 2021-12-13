package za.co.digitalcowboy.event.publisher.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


public class RestUtil {

    public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
    }

    public static HttpHeaders getHeaders(String signature) {


        HttpHeaders requestHeaders = new HttpHeaders();

        //TODO add to config
        requestHeaders.add("x-token", "99725078-d7e8-4dca-a580-11d7619faf77");
        requestHeaders.add("x-signature", signature);
        requestHeaders.add("accept", "application/json");
        requestHeaders.add("content-type", "application/json");

        return requestHeaders;
    }


    public static RestTemplate configureRestTemplate()  {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(httpRequestFactory);

        return restTemplate;
    }


}
