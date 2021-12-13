package za.co.digitalcowboy.event.publisher.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayGeniusResponse{
	private String reference;
	private String redirectUrl;
	private boolean success;
	private String message;

}
