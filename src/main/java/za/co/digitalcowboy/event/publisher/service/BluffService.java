package za.co.digitalcowboy.event.publisher.service;

import za.co.digitalcowboy.event.publisher.domain.BluffHistory;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;

import java.util.List;

public interface BluffService {


    BluffHistory addBluffHistory(BluffHistory bluffHistory) throws ServiceException;


    List<BluffHistory> getUserBluffs(int userId) throws ServiceException;



}
