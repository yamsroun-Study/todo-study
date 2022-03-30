package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoJpaEmRepository implements TodoRepository {

    // 의존성 주입(DI) 방법
    // 1. 생성자 주입
    // 2. Setter 주입
    // 3. 필드 주입

    // @PersistenceContext
    private final EntityManager em;

    // @Autowired
    // public TodoJpaEmRepository(EntityManager em) {
    //     this.em = em;
    // }

    @Override
    public List<Todo> findAll() {
        String jpql = "select t from Todo t"; // JPQL(Java Persistence Query Language)
        return em.createQuery(jpql, Todo.class)
            .getResultList();
    }

    @Override
    public Optional<Todo> findById(Integer id) {
        String jpql = "select t from Todo t where t.id = :id";
        try {
            Todo todo = em.createQuery(jpql, Todo.class)
                .setParameter("id", id)
                .getSingleResult();
            return Optional.of(todo);
            // return Optional.ofNullable(todo);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Todo> findByUserId(String userId) {
        String jpql = "select t from Todo t where t.userId = :userId";
        return em.createQuery(jpql, Todo.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public void save(Todo todo) {
        em.persist(todo);
    }

    @Override
    public void delete(Todo todo) {

    }

    @Override
    public void deleteById(Integer id) {

    }
}
