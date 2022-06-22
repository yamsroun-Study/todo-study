// package jocture.todo.study.factory;
//
// import jocture.todo.study.LoginType;
// import jocture.todo.study.service.*;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Component;
//
// import java.util.NoSuchElementException;
//
// @Component
// @RequiredArgsConstructor
// public class LoginServiceFactoryV1 implements LoginServiceFactory {
//
//     private final WebLoginService webLoginService;
//     private final MobileLoginService mobileLoginService;
//     private final GoogleLoginService googleLoginService;
//
//     @Override
//     public LoginService find(LoginType loginType) {
//         if (loginType == LoginType.WEB) {
//             return webLoginService;
//         }
//         if (loginType == LoginType.MOBILE) {
//             return mobileLoginService;
//         }
//         if (loginType == LoginType.GOOGLE) {
//             return googleLoginService;
//         }
//         throw new NoSuchElementException("Cannot find LoginService of : " + loginType);
//     }
// }
