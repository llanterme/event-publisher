//package za.co.couponzilla.rest.messaging;
//
//
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
//import com.google.gson.Gson;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.integration.aws.support.AwsHeaders;
//import org.springframework.integration.support.MessageBuilder;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Service;
//import org.springframework.util.MimeTypeUtils;
//import za.co.couponzilla.rest.domain.TransactionDeclinedDto;
//import za.co.couponzilla.rest.domain.User;
//
//@Service
//@Slf4j
//public class Sender {
//    private final StreamChannel streamChannel;
//
//    @Autowired
//    public Sender(StreamChannel streamChannel) {
//        this.streamChannel = streamChannel;
//    }
//
//
//    public void sendDeclinedTransaction(final TransactionDeclinedDto transactionDeclinedDto) {
//        System.out.println(("Sending decline payload {}" +  transactionDeclinedDto));
//        streamChannel.declinedCreated().send(MessageBuilder.withPayload(transactionDeclinedDto).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
//    }
//
//    public void masterUserUpdated(@Payload final User user) {
//        System.out.println(("Sending user updated payload {}" + new Gson().toJson(user)));
//
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//
//        try {
//            String json = ow.writeValueAsString(user);
//
//            streamChannel.masterUserUpdated().send(MessageBuilder.withPayload(json).build());
//        } catch (Exception e) {
//
//        }
//
//
//
//
//    }
//
//
//}