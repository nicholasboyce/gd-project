package server.gdproject.Registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import server.gdproject.AppUser.AppUser;
import server.gdproject.AppUser.AppUserDetailsService;
import server.gdproject.AppUser.AppUserRoles;

@Service
public class RegistrationService {

    private AppUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    private RegistrationService(AppUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> register(RegistrationRequest newRegistrationRequest) {

        List<AppUserRoles> roles = Collections.singletonList(new AppUserRoles(null, "PAID", newRegistrationRequest.email())) ;

        if (userDetailsService.userExists(newRegistrationRequest.email())) {
            return ResponseEntity.ok().body("User already exists");
        } else {
            AppUser newUser = new AppUser(
                newRegistrationRequest.company(), 
                newRegistrationRequest.businessType(), 
                newRegistrationRequest.firstName(), 
                newRegistrationRequest.lastName(), 
                newRegistrationRequest.email(), 
                passwordEncoder.encode(newRegistrationRequest.password()), 
                newRegistrationRequest.address(), 
                newRegistrationRequest.city(), 
                newRegistrationRequest.zip(), 
                newRegistrationRequest.country(), 
                newRegistrationRequest.home(), 
                newRegistrationRequest.mobile(), 
                newRegistrationRequest.job(), 
                roles);
            userDetailsService.createUser(newUser);
            return ResponseEntity.ok().body("Registration complete!");
        }

    }
}
