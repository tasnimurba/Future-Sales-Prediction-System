package bd.edu.seu.futuresalespredictionjava.interfaces;

import bd.edu.seu.futuresalespredictionjava.model.User;

import java.util.Optional;

public interface UserInterface {
    User save(User user);
    Optional<User> get(String email);
    void delete(String userId);
}
