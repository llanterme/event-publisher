package za.co.digitalcowboy.event.publisher.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayGeniusRequest{


	private Urls urls;
	private boolean auth;
	private String pageUrlKey;
	private Transaction transaction;
	private Consumer consumer;


}