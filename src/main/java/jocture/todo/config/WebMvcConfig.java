// package jocture.todo.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// @Configuration
// public class WebMvcConfig implements WebMvcConfigurer {
//
//     private final int MAX_AGE_SECS = 3600;
//
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//             .allowedOrigins("http://localhost:3000")
//             // .allowedMethods("GET"/*, "POST", "PUT", "DELET", "OPTIONS"*/)
//             // .allowedHeaders("*")
//             // .allowCredentials(true)
//             /*.maxAge(MAX_AGE_SECS)*/;
//     }
// }
