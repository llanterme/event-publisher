//package za.co.couponzilla.rest.messaging;
//
//
//
//import org.springframework.cloud.stream.annotation.Output;
//import org.springframework.messaging.MessageChannel;
//
//public interface StreamChannel {
//
//
//    String TRANSACTION_DECLINE_PRODUCE_CHANNEL = "declined-transaction-data-analytics-channel";
//
//    String MASTER_USER_UPDATED_PRODUCE_CHANNEL = "master-user-updated-channel";
//
//
//    @Output(StreamChannel.TRANSACTION_DECLINE_PRODUCE_CHANNEL)
//    MessageChannel declinedCreated();
//
//    @Output(StreamChannel.MASTER_USER_UPDATED_PRODUCE_CHANNEL)
//    MessageChannel masterUserUpdated();
//
//
//
//}