package jocture.todo.web.auth;

import jocture.todo.data.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserAthenticationHolder {

    private final ThreadLocal<User> userHolder = new ThreadLocal<>();

    public void setUser(User user) {
        userHolder.set(user);
    }

    public User getUser() {
        return userHolder.get();
    }

    public void remove() {
        userHolder.remove();
    }
}
