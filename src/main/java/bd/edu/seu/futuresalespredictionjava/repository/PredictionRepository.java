package bd.edu.seu.futuresalespredictionjava.repository;

import bd.edu.seu.futuresalespredictionjava.model.Prediction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//graph part
@Repository
public interface PredictionRepository extends MongoRepository<Prediction, String> {
    List<Prediction> findAllByUserId(String userId);
}
