package za.co.digitalcowboy.event.publisher.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import za.co.digitalcowboy.event.publisher.config.ConfigProperties;
import za.co.digitalcowboy.event.publisher.enums.EligibilityReasonEnum;
import za.co.digitalcowboy.event.publisher.response.EligibilityResponse;
import za.co.digitalcowboy.event.publisher.service.EligibilityService;
import za.co.digitalcowboy.event.publisher.service.PropertyBag;
import za.co.digitalcowboy.event.publisher.service.eligibility.DuplicateCouponEligibilityCheck;
import za.co.digitalcowboy.event.publisher.service.eligibility.DuplicateUserEligibilityCheck;
import za.co.digitalcowboy.event.publisher.service.eligibility.UserAuthenticationEligibilityCheck;
import za.co.digitalcowboy.event.publisher.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

@Slf4j
@Service
public class EligibilityServiceImpl implements EligibilityService {

    private ConfigProperties configProperties;

    private TaskExecutor taskExecutor;

    @Autowired
    public EligibilityServiceImpl(TaskExecutor taskExecutor, ConfigProperties configProperties) {
        this.taskExecutor = taskExecutor;
        this.configProperties = configProperties;
    }

    @Override
    public EligibilityResponse evaluateUserRegistrationEligibility(PropertyBag propertyBag) {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("evaluateUserRegistrationEligibility", null));

        List<Function<PropertyBag, EligibilityResponse>> checks = new ArrayList<>();

        checks.add(DuplicateUserEligibilityCheck.executeDuplicateUserEligibilityCheck());

        EligibilityResponse eligibilityResponse = executeFutureTasks(checks, propertyBag);

        log.info(loggerUtil.end("evaluateUserRegistrationEligibility", eligibilityResponse));

        return eligibilityResponse;

    }

    @Override
    public EligibilityResponse evaluateDuplicateCouponEligibility(PropertyBag propertyBag) {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("evaluateDuplicateCouponEligibility", null));

        List<Function<PropertyBag, EligibilityResponse>> checks = new ArrayList<>();

        checks.add(DuplicateCouponEligibilityCheck.executeDuplicateCouponEligibilityCheck());

        EligibilityResponse eligibilityResponse = executeFutureTasks(checks, propertyBag);

        log.info(loggerUtil.end("evaluateDuplicateCouponEligibility", eligibilityResponse));

        return eligibilityResponse;

    }

    @Override
    public EligibilityResponse userAuthenticationEligibilityCheck(PropertyBag propertyBag) {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("UserAuthenticationEligibilityCheck", null));

        List<Function<PropertyBag, EligibilityResponse>> checks = new ArrayList<>();

        checks.add(UserAuthenticationEligibilityCheck.executeUserAuthenticationEligibilityCheck());

        EligibilityResponse eligibilityResponse = executeFutureTasks(checks, propertyBag);

        log.info(loggerUtil.end("UserAuthenticationEligibilityCheck", eligibilityResponse));

        return eligibilityResponse;

    }

    private EligibilityResponse executeFutureTasks(List<Function<PropertyBag, EligibilityResponse>> checks, PropertyBag propertyBag) {

        //TODO - Write all the decline reason to a database for reporting.
        List<FutureTask<EligibilityResponse>> checkFutureTasks = new ArrayList<>();
        List<EligibilityResponse> eligibilityResponses = new ArrayList<>();
        EligibilityResponse mainResponse;

        try {

            for (Function<PropertyBag, EligibilityResponse> check : checks) {
                FutureTask<EligibilityResponse> checkFutureTask = new FutureTask<>(() -> {
                    return check.apply(propertyBag);
                });

                checkFutureTasks.add(checkFutureTask);

                taskExecutor.execute(checkFutureTask);
            }

            for (FutureTask<EligibilityResponse> futureTask : checkFutureTasks) {
                try {

                    EligibilityResponse eligibilityCheckResponse = futureTask.get();
                    eligibilityResponses.add(eligibilityCheckResponse);

                } catch (Exception e) {

                    throw e;

                }

            }

            EligibilityResponse topResponse = eligibilityResponses.stream()
                    .filter(response -> response.getEligibilityReason() != EligibilityReasonEnum.SUCCESSFUL)
                    .sorted((response1, response2) -> {
                        return response1.getEligibilityReason().getPriority() - response2.getEligibilityReason().getPriority();
                    })
                    .findFirst()
                    .orElse(null);

            if (topResponse != null) {
                mainResponse = topResponse;
            } else {
                mainResponse = new EligibilityResponse();
                mainResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
            }

        } catch (Exception e) {

            EligibilityResponse eligibilityResponse = new EligibilityResponse();
            eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.UNHANDLED_ELIGIBILITY_EXCEPTION);

            return eligibilityResponse;
        }

        return mainResponse;

    }

}
