package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        String jpql = "select t from Todo t order by t.id desc"; // JPQL(Java Persistence Query Language)
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
            // return Optional.ofNullable(todo)
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Todo> findByUserId(String userId) {
        String jpql = "select t from Todo t where t.userId = :userId order by t.id desc";
        return em.createQuery(jpql, Todo.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public void save(Todo todo) {
        em.persist(todo);
    }

    // TDD (Test-Driven Development) -> 테스트 주도 개발
    // -> 실제 메인 코드보다 테스트 코드를 먼저 만드는 개발 방법론

    @Override
    public void delete(Todo todo) { // Java에서는 Method Signature (메소드 시그니처)
        deleteById(todo.getId());
    }

    // 1. Best (Method Reference)
    @Override
    public void deleteById(Integer id) {
        findById(id).ifPresent(em::remove);
    }

    // 2. Not Bad (Lambda Expression)
    public void deleteById_notBad(Integer id) {
        findById(id).ifPresent(entity -> em.remove(entity));
    }

    // 3. Worst (Optional스럽지 못한 사용)
    public void deleteById_worst(Integer id) {
        Optional<Todo> result = findById(id);
        if (result.isPresent()) {
            Todo todo = result.get();
            em.remove(todo);
        }
    }
}
