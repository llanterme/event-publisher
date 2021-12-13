package za.co.digitalcowboy.event.publisher.service;

import za.co.digitalcowboy.event.publisher.domain.User;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;

import java.util.List;

public interface UserService {


    //@Retryable(value = ServiceException.class,maxAttempts = 2, backoff = @Backoff(delay = 100))
    User registerUser(User user) throws ServiceException;

    User findUser(String emailAddress) throws ServiceException;

    List<User> getAllUsers() throws ServiceException;

    User authUser(User user) throws ServiceException;


//    @Recover
//    void recover(ServiceException e, User user);
}
