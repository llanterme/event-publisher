package za.co.digitalcowboy.event.publisher.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction{


	private String reference;
	private int amount;
	private String currency;


}