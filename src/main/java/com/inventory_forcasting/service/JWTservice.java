package com.inventory_forcasting.service;


import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableScheduling
public class JWTservice {


    private final Map<String, LocalDateTime> blacklist;
    private final PriorityQueue<Object[]> blacklistQueue;

    @Value("${jwt.expiration}")
    private long exptime;//24 hrs

    private String secretkey;

    public JWTservice(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        blacklist=new ConcurrentHashMap<>();
        blacklistQueue = new PriorityQueue<>(Comparator.comparing(a -> (LocalDateTime) a[1]));
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretkey.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails,long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",userId);
        claims.put("role",userDetails.getAuthorities().stream()
                .map(k-> k.getAuthority())
                .collect(Collectors.toList()));
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600 * exptime ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public long extractUserId(String token) {
//        return extractClaim(token, claims -> Long.parseLong(claims.get("userId").toString()));
        return extractAllClaims(token).get("userId",Long.class);
    }

    public List<? extends GrantedAuthority> extractRole(String token) {
//        return extractClaim(token, claims -> (Collection<? extends GrantedAuthority>) claims.get("role"));
        return extractAllClaims(token).get("role",List.class);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public void addTokenToBlacklist(String token) {
        log.info("Adding token to blacklist: {}", token.substring(0,10));
        LocalDateTime expiryTime = extractExpiration(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        expiryTime=expiryTime.plus(Duration.ofHours(24));
        blacklist.put(token,expiryTime);//24 hour blacklist time same as jwt expiration time
        Object ob[]=new Object[2];
        ob[0]=token;
        ob[1]=expiryTime;
        blacklistQueue.add(ob);
    }

    //check token is blacklisted?
    public Boolean isTokenBlacklisted(String token) {
        log.info("Checking if token is blacklisted: {}", token.substring(0,10));
        return blacklist.containsKey(token);
    }

    //just a ttl cleaner for the hashmap eevry 1hr
    @Scheduled(fixedRate = 3_600_000)
//    @Scheduled(fixedRate = 60000)
    private void removeExpiredTokens() {
        log.info("Removing expired tokens!!");
        while (!blacklistQueue.isEmpty()) {
            Object[] ob = blacklistQueue.peek();
            if (((LocalDateTime) ob[1]).isBefore(LocalDateTime.now())) {
                blacklist.remove((String) ob[0]);
                blacklistQueue.poll();
            } else {
                break;
            }
        }
    }
}
