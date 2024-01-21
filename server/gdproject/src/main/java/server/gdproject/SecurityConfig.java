package server.gdproject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                 .authorizeHttpRequests(request -> request
                         .requestMatchers("/landrecords/**")
                         .hasRole("PAID"))
                 .csrf(csrf -> csrf.disable())
                 .httpBasic(Customizer.withDefaults());
         return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails sarah = users
            .username("sarah1")
            .password(passwordEncoder.encode("abc123"))
            .roles("ADMIN", "PAID")
            .build();
        UserDetails melissa = users
            .username("melissa2")
            .password(passwordEncoder.encode("xyz321"))
            .roles("PAID")
            .build();
        UserDetails rory = users
            .username("roryg")
            .password(passwordEncoder.encode("gilmore"))
            .roles("NON-PAID")
            .build();
        return new InMemoryUserDetailsManager(sarah, melissa, rory);
    }

}
