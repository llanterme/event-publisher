//package za.co.couponzilla.rest.config;
//
//
//import com.google.gson.Gson;
//import org.slf4j.LoggerFactory;
//import org.springframework.integration.config.GlobalChannelInterceptor;
//import org.springframework.integration.support.MutableMessageHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.messaging.MessagingException;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.messaging.support.ChannelInterceptorAdapter;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.stereotype.Component;
//
//import java.util.logging.Logger;
//
//@Component
//@GlobalChannelInterceptor
//public class Interceptor implements ChannelInterceptor {
//
//    private Message<?> getMessage(Message<?> message) {
//        Object payload = message.getPayload();
//        if (payload instanceof MessagingException) {
//            MessagingException e = (MessagingException) payload;
//            Message<?> failedMessage = e.getFailedMessage();
//            return failedMessage != null ? failedMessage : message;
//        }
//
//        MessageHeaderAccessor headers = MessageHeaderAccessor.getMutableAccessor(message);
////        System.out.println(headers.getContentType());
////        MessageHeaders s = headers.getMessageHeaders();
////        System.out.println(headers.isMutable());
//
//        return message;
//    }
//
//    private MessageHeaderAccessor mutableHeaderAccessor(Message<?> message) {
//        MessageHeaderAccessor headers = MessageHeaderAccessor.getMutableAccessor(message);
//        return headers;
//    }
//
//
//    @Override
//    public Message<?> preSend(Message<?> msg, MessageChannel mc) {
//        System.out.println("In preSend");
//
//        Message<?> retrievedMessage = getMessage(msg);
//
//        MessageHeaderAccessor headers = mutableHeaderAccessor(retrievedMessage);
//        headers.removeHeaders("*", "contentType");
//
//        return retrievedMessage;
//    }
//
//    @Override
//    public void postSend(Message<?> msg, MessageChannel mc, boolean bln) {
//        System.out.println("In postSend");
//        System.out.println(new Gson().toJson(msg));
//
//    }
//
//    @Override
//    public void afterSendCompletion(Message<?> msg, MessageChannel mc, boolean bln, Exception excptn) {
//        System.out.println("In afterSendCompletion");
//    }
//
//    @Override
//    public boolean preReceive(MessageChannel mc) {
//        System.out.println("In preReceive");
//        return true;
//    }
//
//    @Override
//    public Message<?> postReceive(Message<?> msg, MessageChannel mc) {
//        System.out.println("In postReceive");
//        return msg;
//    }
//
//    @Override
//    public void afterReceiveCompletion(Message<?> msg, MessageChannel mc, Exception excptn) {
//        System.out.println("In afterReceiveCompletion");
//    }
//
//}
