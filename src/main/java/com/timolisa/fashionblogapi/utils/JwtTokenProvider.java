package com.timolisa.fashionblogapi.utils;

import com.timolisa.fashionblogapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.timolisa.fashionblogapi.utils.SecurityConstants.EXPIRATION_TIME;

@Component
@Slf4j
public class JwtTokenProvider {
    private final HttpServletRequest request;

    @Autowired
    public JwtTokenProvider(HttpServletRequest request) {
        this.request = request;
    }

    private static String generateSecret() {
        // used to represent binary data (secret key) in an ASCII string format.
        return DatatypeConverter.printBase64Binary(new byte[512 / 8]);
    }

    private static Key generateKey() {
        byte[] secretKeyInBytes =
                DatatypeConverter.parseBase64Binary(generateSecret());
        return new SecretKeySpec(secretKeyInBytes, "HmacSHA512");
    }

    public String generateToken(final Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        return createToken(claims, user.getUsername());
    }

    public Claims getAllClaims(String token) {
            // https://github.com/jwtk/jjwt#jws-create-key
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        // reading a jws
        // https://www.loginradius.com/blog/engineering/guest-post/what-are-jwt-jws-jwe-jwk-jwa/
        boolean status = false;
        Claims claims = getAllClaims(token);
        try {
            // https://github.com/jwtk/jjwt#jws-create-key
            String username = (String) claims.get("username");

            if (username.equals("")) {
                log.error("Missing username in token");
                return status;
            }

            if (!username.equals(userDetails.getUsername())) {
                log.warn("Username in token does not match the provided user details");
                return status;
            }

            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                log.warn("Token has expired {}", token);
                return status;
            }

            status = true;
            return status;
        } catch (JwtException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return status;
        }
    }

    public Long getUserIdFromToken(String token) {
        return getAllClaims(token)
                .get("userId", Long.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims
                .get("username", String.class);
    }

    private String createToken(Map<String, Object> claims, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        Key key = generateKey();
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuer(request.getRequestURL().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getTokenFromContext() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getCredentials().toString();
    }
}
