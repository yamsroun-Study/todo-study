package jocture.todo.repository;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import jocture.todo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJpaEmRepository implements UserRepository {

    // 컴포넌트 주입방법 : 1)생성자주입 2)Setter주입 3)필드주입

    private final EntityManager em;

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String jpql = "select u from User u where email = :email";
        try {
            User user = em.createQuery(jpql, User.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        String jpql = "select 1 from User u where email = :email";
        try {
            Integer result = em.createQuery(jpql, Integer.class)
                .setParameter("email", email)
                .getSingleResult();
            return result != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String jpql = "select u from User u where email = :email and password = :password";
        try {
            User user = em.createQuery(jpql, User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
