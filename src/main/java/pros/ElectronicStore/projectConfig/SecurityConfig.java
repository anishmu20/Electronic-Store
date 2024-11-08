package pros.ElectronicStore.projectConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user1 = User.builder()
                .username("anish")
                .password(passwordEncoder().encode("anish"))
                .roles("Normal")
                .build();

        UserDetails user2 =User.builder()
                .username("Akhil")
                .password(passwordEncoder().encode("akhil123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager( user1,  user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
