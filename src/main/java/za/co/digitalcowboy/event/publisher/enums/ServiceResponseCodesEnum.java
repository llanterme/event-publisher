package za.co.digitalcowboy.event.publisher.enums;


import java.io.Serializable;

/**
 * This class is used for core payment response code identifier
 *
 */
public enum ServiceResponseCodesEnum implements Serializable {

	UNABLE_TO_REGISTER_USER(001, "Unable to register user"),
	UNABLE_TO_GET_COUPONS(002, "Unable to get coupons"),
	UNABLE_TO_ADD_COUPON_HISTORY(003, "Unable to add coupon history"),
	UNABLE_TO_ADD_FILTER_COUPONS(004, "Unable to filter coupon"),
	UNABLE_TO_GET_INDUSTRIES(005, "Unable to get industries"),
	UNABLE_AUTH_USER(006, "Unable auth user"),
	UNABLE_TO_ADD_BLUFF_HISTORY(07, "Unable to add bluff history"),
	UNABLE_TO_GET_BLUFF_HISTORY(8, "Unable to get bluff history"),
	UNABLE_TO_FIND_USER(9, "Unable to find user"),
	UNABLE_TO_GET_USERS_LIST(10, "Unable to get users list"),
	UNABLE_TO_CREATE_PAYMENT_ENDPOINT(0010, "Unable to create payment end point"),
	UNABLE_TO_GENERATE_SECURE_SIGNATURE(0010, "Unable to generate secure signature"),
	SUCCESSFUL(0, "Success"),
	API_AUTHENTICATION_ERROR(1001, "Invalid credentials");

	private int errorCode;
	private String message;

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	 ServiceResponseCodesEnum(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

}