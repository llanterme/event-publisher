package za.co.digitalcowboy.event.publisher.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import za.co.digitalcowboy.event.publisher.entity.IndustryEntity;


@Repository
public interface IndustryRepository extends CrudRepository<IndustryEntity,Integer> {


    //Random comment
    IndustryEntity getIndustryEntitiesByIndustryId(int industryId);

}
