package pros.ElectronicStore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthencationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;


    private Logger logger= LoggerFactory.getLogger(JWTAuthencationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader=request.getHeader("Authorization");
        logger.info(requestHeader);
        String username=null;
        String token =null;

        if (requestHeader!=null && requestHeader.startsWith("Bearer ")){
            token=requestHeader.substring(7);
            try{
                username=jwtHelper.getUsernameFromToken(token);
            }catch (IllegalArgumentException ex){
                logger.info("IllegalArgument while fetching the user "+ex.getMessage());
            }
            catch (ExpiredJwtException ex){
                logger.info("Given jwt is expired "+ex.getMessage());
            }
            catch (MalformedJwtException ex){
                logger.info("some changes has been made to original token "+ex.getMessage());
            }

        }
        else{
            logger.info("Invalid Header ");
        }
        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
