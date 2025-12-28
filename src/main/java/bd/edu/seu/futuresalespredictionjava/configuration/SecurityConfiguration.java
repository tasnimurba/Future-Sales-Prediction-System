package bd.edu.seu.futuresalespredictionjava.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/admin").hasAnyRole("ADMIN", "USER") // hasAnyRole(...) allows multiple roles to access the same path.
                        .requestMatchers("/api/prediction-trends").permitAll() // Make this API public
                        .anyRequest()
                        .authenticated() // Allow all authenticated users for other routes
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/dashboard", true) // Redirect to /home on successful login
                )
                .logout(config -> config
                        //.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // Enable GET for logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true") // Redirect to login page after logout
                        .invalidateHttpSession(true) // Invalidate session after logout
                        .clearAuthentication(true) // Clear authentication
                        .deleteCookies("JSESSIONID") // Delete session cookie
                        .permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Secure password storage
    }

}
