package jocture.todo.controller.session;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// 객체 생성 방지 방안
// 1. 추상 클래스로 만든다.
// 2. 생성자를 private으로 만든다.

//public abstract class SessionConst {
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionConst {

    public static final String SESSION_ID = "mySessionId";
    public static final String SESSION_USER_KEY = "userData";
}
