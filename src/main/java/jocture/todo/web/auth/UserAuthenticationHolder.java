package jocture.todo.web.auth;

import jocture.todo.data.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationHolder implements AuthenticationHolder {

    private User user;

    @Override
    public User get() {
        return user;
    }

    @Override
    public void set(Object user) {
        this.user = (User) user;
    }
}
