package com.example.webrtc.util;

import com.example.webrtc.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey".getBytes());

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(Long id, String username, String name) {
        return Jwts.builder()
                .setSubject(username)
                .setId(String.valueOf(id))
                .claim("role", "USER")
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 86400000)
                )
                .signWith(key)
                .compact();
    }

    public User extractUser(String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = body.getSubject();
        Long id = Long.valueOf(body.getId());
        String name = body.get("name", String.class);
        User user = new User(id, username, name, null);
        return user;
    }

    public User validateSignalAndExtractUser(WebSocketSession session) throws IOException {
        try {

            String query = session.getUri().getQuery();

            if (query == null || !query.startsWith("token=")) {
                session.close(
                        CloseStatus.POLICY_VIOLATION
                                .withReason("Missing token")
                );

                return null;
            }

            String token = query.substring(6);

            if (!validate(token)) {

                session.close(
                        CloseStatus.POLICY_VIOLATION
                                .withReason("Invalid token")
                );

                return null;
            }

            return extractUser(token);


        } catch (Exception e) {
            session.close(
                    CloseStatus.SERVER_ERROR
                            .withReason("Authentication failed")

            );

            return null;
        }
    }

}
