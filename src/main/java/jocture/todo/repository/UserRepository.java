package jocture.todo.repository;

import java.util.Optional;
import jocture.todo.entity.User;

public interface UserRepository {

    void save(User user);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

}
