package bd.edu.seu.futuresalespredictionjava.service;

import bd.edu.seu.futuresalespredictionjava.interfaces.UserInterface;
import bd.edu.seu.futuresalespredictionjava.model.User;
import bd.edu.seu.futuresalespredictionjava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService implements UserInterface {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> get(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Override
    public void delete(String userId) {
        userRepository.deleteByUserId(userId);
    }
}
