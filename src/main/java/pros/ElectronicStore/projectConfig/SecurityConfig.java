package pros.ElectronicStore.projectConfig;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import pros.ElectronicStore.security.JWTAuthencationFilter;
import pros.ElectronicStore.security.JwtAuthencationEntryPoint;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthencationEntryPoint entryPoint;

    @Autowired
    private JWTAuthencationFilter  filter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(httpSecurityCorsConfigurer ->
                httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration=new CorsConfiguration();
                        corsConfiguration.addAllowedOrigin("http://localhost:5000");
                        corsConfiguration.setMaxAge(4000L);
                        corsConfiguration.setAllowedMethods(List.of("*"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        corsConfiguration.setAllowCredentials(true);

                        return corsConfiguration;
                    }
                })

                );
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        httpSecurity.authorizeHttpRequests(
                request->{
                    request

                            .requestMatchers(HttpMethod.DELETE,"/users/**").hasRole(AppConstants.ROLE_ADMIN)
                            .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole(AppConstants.ROLE_ADMIN,AppConstants.ROLE_NORMAL)
                            .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                            .requestMatchers("/products/**").hasRole(AppConstants.ROLE_ADMIN)
                            .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/users").permitAll()
                            .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                            .requestMatchers("/categories/**").hasRole(AppConstants.ROLE_ADMIN)
                            .requestMatchers(HttpMethod.POST,"/auth/generate-token").permitAll()
                            .requestMatchers("/auth/**").authenticated()
                            .anyRequest().permitAll();
                }
                );

        // Security type --

        // entry point if error Occurred
        httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(entryPoint));
        // session management
        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // adding JWT filter before UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(){
//
//        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
//        return daoAuthenticationProvidAppConstants.ROLE_ADMIN
//    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
