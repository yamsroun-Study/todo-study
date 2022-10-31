package jocture.todo.web.auth;

import jocture.todo.data.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TokenProviderTest {

    TokenProvider tokenProvider = new TokenProvider();

    static String TEST_USER_ID = "abcdef";
    static String createdToken;

    //@Order(1)
    @Test
    void create() {
        //given
        User user = User.builder()
            .id(TEST_USER_ID)
            .email("yamsroun@gmail.com")
            .build();
        //when
        createdToken = tokenProvider.create(user);
        //then
        System.out.println("token = " + createdToken);
        assertThat(createdToken).isNotEmpty();
    }

    //@Order(2)
    @Test
    void validateAndGetUserId() {
        //given
        create();
        //when
        String userId = tokenProvider.validateAndGetUserId(createdToken);
        //then
        System.out.println("userId = " + userId);
        assertThat(userId).isEqualTo(TEST_USER_ID);
    }
}
