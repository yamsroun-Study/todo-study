package jocture.todo.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// JPA
// -> Java의 표준 ORM
// -> 스펙, 규격, 표준
// -> 구현체가 필요하다.
//   -> Hibernate(디폴트), EclipseLink, ...
// -> 스프링 데이터 JPA : 스프링 진영에서 지원하는 라이브러리

@Entity
// @Table(name = "tbl_todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 스펙
@AllArgsConstructor(access = AccessLevel.PRIVATE) // @Builder 스펙
@Builder
@Getter @Setter
@ToString
public class Todo {

    // Primitive Type (원시/기본 타입) -> int, long, boolean, ...
    // Wrapper Type -> Integer, Long, Boolean, String, ...
    // Reference Type -> 기타 객체들 ...

    @Id @GeneratedValue
    private Integer id;

    private String userId;

    private String title;

    private boolean done;
}
