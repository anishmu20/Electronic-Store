package pros.ElectronicStore.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtHelper {

   private static final long JWT_TOKEN_VALIDITY = 60*60;

   private final String  SECRET_KEY="dfbhfdbhjhgbjlfgbhjbgjkbgfdjhbfedjgbriejghreighefdujghfdjgghjgh";

   public String getUsernameFromToken(String token){
       String username= getClaimsFromToken(token).getSubject();
       return username;
   }

   public Claims getClaimsFromToken(String token){
       Claims claims= Jwts.parserBuilder().
               setSigningKey(SECRET_KEY.getBytes())
               .build().parseClaimsJws(token).getBody();
       return claims;
   }

   public Boolean isTokenExpired(String token){
       Claims claims = getClaimsFromToken(token);
       Date expiration = claims.getExpiration();
       return expiration.before(new Date());
   }

   public String generateToken(UserDetails userDetails){
       Map<String,Object> claims=new HashMap<>();
       return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
               .signWith(new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName()),SignatureAlgorithm.HS256)
               .compact();
   }








}
