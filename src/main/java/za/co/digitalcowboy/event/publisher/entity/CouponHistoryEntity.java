package za.co.digitalcowboy.event.publisher.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon_history")
public class CouponHistoryEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_history_id")
    private int couponHistoryId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "coupon_id")
    private int couponId;

    @Column(name = "date_added")
    private String dateAdded;


}


