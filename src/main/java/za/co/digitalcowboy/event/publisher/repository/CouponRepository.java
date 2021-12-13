package za.co.digitalcowboy.event.publisher.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import za.co.digitalcowboy.event.publisher.entity.CouponEntity;

import java.util.List;

@Repository
public interface CouponRepository extends CrudRepository<CouponEntity,Integer> {

    List<CouponEntity> getCouponEntitiesByIndustryId(int industryId);


}
