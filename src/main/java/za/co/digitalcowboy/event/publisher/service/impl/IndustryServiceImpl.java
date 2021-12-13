package za.co.digitalcowboy.event.publisher.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.digitalcowboy.event.publisher.domain.Industry;
import za.co.digitalcowboy.event.publisher.enums.ServiceResponseCodesEnum;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;
import za.co.digitalcowboy.event.publisher.repository.IndustryRepository;
import za.co.digitalcowboy.event.publisher.service.IndustryService;
import za.co.digitalcowboy.event.publisher.utils.DozerHelper;
import za.co.digitalcowboy.event.publisher.utils.LoggerUtil;

import java.util.List;

import static za.co.digitalcowboy.event.publisher.utils.DozerHelper.toList;

@Service
@Slf4j
public class IndustryServiceImpl implements IndustryService {

    private IndustryRepository industryRepository;

    @Autowired
    public IndustryServiceImpl(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    @Override
    public List<Industry> getIndustries() throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        List<Industry> industryList;

        try {
            log.info(loggerUtil.start("get industry request", null));
            industryList = DozerHelper.map(new DozerBeanMapper(), toList(industryRepository.findAll()), Industry.class);
            log.info(loggerUtil.end("get industry request", null));

            return industryList;

        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to get industries from the database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_GET_INDUSTRIES);

        }
    }
}
