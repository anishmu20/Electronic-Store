package pros.ElectronicStore.projectConfig;

import jakarta.servlet.http.HttpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails user1 = User.builder()
//                .username("anish")
//                .password(passwordEncoder().encode("anish"))
//                .roles("Normal")
//                .build();
//
//        UserDetails user2 =User.builder()
//                .username("Akhil")
//                .password(passwordEncoder().encode("akhil123"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager( user1,  user2);
//    }
    // below show the basic implementation of the Basic Auth
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        return daoAuthenticationProvider;

    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
