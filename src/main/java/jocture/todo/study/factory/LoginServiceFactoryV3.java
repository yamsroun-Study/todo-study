package jocture.todo.study.factory;

import jocture.todo.study.LoginType;
import jocture.todo.study.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginServiceFactoryV3 implements LoginServiceFactory {

    private final List<LoginService> loginServices;
    // 1. new 하지 않으면 -> LinkedHashMap 객체 주입
    // 2. 일반적으로는 HashMap 사용
    // 3. Map의 키가 Enum 타입일 때는, HashMap 대신 EnumMap을 사용하는게 더 빠름
    // private final Map<LoginType, LoginService> cachedLoginServiceMap = new EnumMap<>(LoginType.class);
    // 캐시 종류 : Memcached, Redis, EhCache

    // 오류 발견 : 1.컴파일 시 발견 -> 2. 애플리케이션 로딩 시 발견 -> 3. 런타임 시 발견(최악)

    @PostConstruct
    void postConstruct() {
        EnumSet<LoginType> loginTypes = EnumSet.allOf(LoginType.class);
        loginTypes.forEach(loginType -> {
            try {
                LoginServiceCache.put(loginType, getLoginService(loginType));
            } catch (NoSuchElementException e) {
                log.warn("Cannot find LoginService of : {}", loginType);
            }
        });
    }

    private LoginService getLoginService(LoginType loginType) {
        return loginServices.stream()
            .filter(service -> service.supports(loginType))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Cannot find LoginService of : " + loginType));
    }

    @Override
    public LoginService find(LoginType loginType) {
        // log.debug(">>> cachedLoginServiceMap: {}", cachedLoginServiceMap.getClass());
        // 찐 인싸..!!!
        return LoginServiceCache.get(loginType)
            .orElseThrow(() -> new NoSuchElementException("Cannot find LoginService of : " + loginType));
    }

    private static class LoginServiceCache {

        private static final Map<LoginType, LoginService> loginServiceMap = new EnumMap<>(LoginType.class);

        public static Optional<LoginService> get(LoginType loginType) {
            return Optional.ofNullable(loginServiceMap.get(loginType));
            // return loginServiceMap.get(loginType);
        }

        public static void put(LoginType loginType, LoginService loginService) {
            loginServiceMap.put(loginType, loginService);
        }

    }

}
