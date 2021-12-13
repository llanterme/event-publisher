package za.co.digitalcowboy.event.publisher.service;

import za.co.digitalcowboy.event.publisher.domain.Industry;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;

import java.util.List;

public interface IndustryService {

    List<Industry> getIndustries() throws ServiceException;
}
