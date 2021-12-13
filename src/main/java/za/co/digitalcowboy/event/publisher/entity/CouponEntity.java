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
@Table(name = "coupon")
public class CouponEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private int couponId;

    @Column(name = "coupon")
    private String coupon;

    @Column(name = "organization_id")
    private int organizationId;

    @Column(name = "description")
    private String description;

    @Column(name = "industry_id")
    private int industryId;

    @Column(name = "active")
    private int active;

    
}


