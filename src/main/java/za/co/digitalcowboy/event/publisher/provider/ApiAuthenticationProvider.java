package za.co.digitalcowboy.event.publisher.provider;

import za.co.digitalcowboy.event.publisher.exceptions.ApiAuthenticationException;

public interface ApiAuthenticationProvider {
	
	void authenticateApi(String authorization) throws ApiAuthenticationException;

}
