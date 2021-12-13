package za.co.digitalcowboy.event.publisher.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import za.co.digitalcowboy.event.publisher.entity.CouponHistoryEntity;

@Repository
public interface CouponHistoryRepository extends CrudRepository<CouponHistoryEntity,Integer> {

    CouponHistoryEntity findByCouponId(int couponId);


}
