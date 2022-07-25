package jocture.todo.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 스펙
@AllArgsConstructor(access = AccessLevel.PRIVATE) //Builder에서 필요
@Builder
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String username;

    private String email;

    private String password;
}
