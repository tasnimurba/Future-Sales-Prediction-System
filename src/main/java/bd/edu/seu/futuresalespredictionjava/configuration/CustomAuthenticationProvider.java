package bd.edu.seu.futuresalespredictionjava.configuration;

import bd.edu.seu.futuresalespredictionjava.model.User;
import bd.edu.seu.futuresalespredictionjava.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException //This is the method Spring calls to check the user's login details.
    {
        //Get email and password from login form
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();


        User user = userRepository.findFirstByEmail(email).get();



        //Check if user exists and password matches
        //If no student or wrong password → throw error = login fails.
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UsernameNotFoundException("Invalid email or password");
        }


//Converts student roles (like "ROLE_USER") into Spring's format
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        System.out.println("User roles: " + user.getRoles());  // Make sure the user has roles assigned



// Return an authenticated token with roles if needed
        //Returns a successful login token with roles attached
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }


    //This tells Spring:
//"I only handle login requests that use username and password
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

