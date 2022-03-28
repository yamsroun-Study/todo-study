package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
}
