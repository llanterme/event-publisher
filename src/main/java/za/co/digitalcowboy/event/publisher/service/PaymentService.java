package za.co.digitalcowboy.event.publisher.service;

import za.co.digitalcowboy.event.publisher.domain.payment.PayGeniusResponse;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;

public interface PaymentService {

    PayGeniusResponse createPayEndPoint() throws ServiceException;
}
