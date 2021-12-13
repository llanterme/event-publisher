package za.co.digitalcowboy.event.publisher.exceptions;

import lombok.Getter;
import lombok.Setter;
import za.co.digitalcowboy.event.publisher.enums.ServiceResponseCodesEnum;

@Getter
public class ServiceException extends Exception {

	@Getter
	@Setter
	private ServiceResponseCodesEnum responseCodesEnum;

	public ServiceException(ServiceResponseCodesEnum responseCodesEnum) {
        this.responseCodesEnum = responseCodesEnum;

	}




}