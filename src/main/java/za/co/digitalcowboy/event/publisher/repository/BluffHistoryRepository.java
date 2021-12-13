package za.co.digitalcowboy.event.publisher.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import za.co.digitalcowboy.event.publisher.entity.BluffHistoryEntity;

import java.util.List;

@Repository
public interface BluffHistoryRepository extends CrudRepository<BluffHistoryEntity,Integer> {

    List<BluffHistoryEntity> getAllByUserId(int userId);

}
