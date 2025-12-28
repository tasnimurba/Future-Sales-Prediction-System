package bd.edu.seu.futuresalespredictionjava.repository;

import bd.edu.seu.futuresalespredictionjava.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {
    void deleteByUserId(String userId);
    //Optional<User> findByEmail(String email);
    Optional<User> findFirstByEmail(String email);

}
