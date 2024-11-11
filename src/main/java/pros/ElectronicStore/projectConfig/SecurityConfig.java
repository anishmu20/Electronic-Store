package pros.ElectronicStore.projectConfig;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        httpSecurity.authorizeHttpRequests(
                request->{
                    request

                            .requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole("ADMIN","NORMAL")
                            .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                            .requestMatchers("/products/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/users").permitAll()
                            .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                            .requestMatchers("/categories/**").hasRole("ADMIN");
                }
                );

        // Security type -- as of now it is basic
        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
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
