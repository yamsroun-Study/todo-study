package jocture.todo.web.auth;

import io.jsonwebtoken.*;
import jocture.todo.data.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String SECRET_KEY = "Y1A2M3S4R5O6U7N8";

    //JWT 토큰 생성 후 반환
    public String create(User user) {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
            //Header, Signature
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            //Payload
            .setSubject(user.getId()) //sub
            .setIssuer("Todo App") //iss
            .setIssuedAt(new Date()) //iat
            .setExpiration(expiryDate) //exp
            .compact();
    }

    //JWT 토큰에서 회원ID 반환
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token) //Caution: vs. parseClaimsJw"t"()
            .getBody();
        return claims.getSubject();
    }
}
