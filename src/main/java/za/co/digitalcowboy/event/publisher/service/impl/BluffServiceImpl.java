package za.co.digitalcowboy.event.publisher.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.digitalcowboy.event.publisher.domain.BluffHistory;
import za.co.digitalcowboy.event.publisher.entity.BluffHistoryEntity;
import za.co.digitalcowboy.event.publisher.enums.ServiceResponseCodesEnum;
import za.co.digitalcowboy.event.publisher.exceptions.ServiceException;
import za.co.digitalcowboy.event.publisher.repository.BluffHistoryRepository;
import za.co.digitalcowboy.event.publisher.service.BluffService;
import za.co.digitalcowboy.event.publisher.utils.DozerHelper;
import za.co.digitalcowboy.event.publisher.utils.LoggerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static za.co.digitalcowboy.event.publisher.utils.DozerHelper.toList;

@Service
@Slf4j
public class BluffServiceImpl implements BluffService {
    private BluffHistoryRepository bluffHistoryRepository;


    @Autowired
    public BluffServiceImpl(BluffHistoryRepository bluffHistoryRepository) {
        this.bluffHistoryRepository = bluffHistoryRepository;

     }

    @Override
    public BluffHistory addBluffHistory(BluffHistory bluffHistory) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        try {
            log.info(loggerUtil.start("add bluffs history ", bluffHistory));
            bluffHistory.setDateAdded(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            BluffHistoryEntity bluffHistoryEntity = bluffHistoryRepository.save(new DozerBeanMapper().map(bluffHistory, BluffHistoryEntity.class));
            bluffHistory.setBluffHistoryId(bluffHistoryEntity.getBluffHistoryId());
            log.info(loggerUtil.end("add bluff history ", bluffHistory));
            return bluffHistory;
        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to add bluff history to database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_ADD_BLUFF_HISTORY);
        }

    }

    @Override
    public List<BluffHistory> getUserBluffs(int userId) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<BluffHistory> bluffHistoryList;

        try {
            log.info(loggerUtil.start("get bluff history request for id", userId));
            bluffHistoryList = DozerHelper.map(new DozerBeanMapper(), toList(bluffHistoryRepository.getAllByUserId(userId)), BluffHistory.class);
            log.info(loggerUtil.end("get bluff history request for id", userId));

            return bluffHistoryList;

        } catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to get get bluff history from the database ", e.getMessage()));
            throw new ServiceException(ServiceResponseCodesEnum.UNABLE_TO_GET_BLUFF_HISTORY);

        }
    }


}
