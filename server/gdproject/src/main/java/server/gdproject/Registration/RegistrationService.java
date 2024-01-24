package server.gdproject.Registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private JdbcUserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;

    private RegistrationService(JdbcUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> register(RegistrationRequest newRegistrationRequest) {
        User.UserBuilder users = User.builder();
        UserDetails newUser = users
            .username(newRegistrationRequest.username())
            .password(passwordEncoder.encode(newRegistrationRequest.password()))
            .roles("PAID")
            .build();
        if (userDetailsManager.userExists(newUser.getUsername())) {
            return ResponseEntity.ok().body("User already exists");
        } else {
            userDetailsManager.createUser(newUser);
            return ResponseEntity.ok().body("Registration complete!");
        }

    }
}
