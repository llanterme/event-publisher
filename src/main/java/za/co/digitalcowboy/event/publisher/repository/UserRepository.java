package za.co.digitalcowboy.event.publisher.repository;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.digitalcowboy.event.publisher.entity.UserEntity;


@Repository
public interface UserRepository extends CrudRepository<UserEntity,Integer> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM UserEntity c WHERE c.emailAddress = :emailAddress")
    boolean validateUserAlreadyRegistered(@Param("emailAddress") String emailAddress);

    UserEntity findUserByEmailAddress(String emailAddress);


    @Query("SELECT p FROM UserEntity p WHERE p.emailAddress = :emailAddress")
    UserEntity authenticateUser(@Param("emailAddress") String emailAddress);


}
