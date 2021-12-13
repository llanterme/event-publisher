package za.co.digitalcowboy.event.publisher.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consumer{

	private Address address;
	private String surname;
	private String name;
  private String email;


}