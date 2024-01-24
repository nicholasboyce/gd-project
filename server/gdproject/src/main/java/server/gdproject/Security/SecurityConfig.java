package server.gdproject.Security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                 .authorizeHttpRequests(request -> request
                         .requestMatchers("/landrecords/**").hasRole("PAID")
                         .requestMatchers("/register").permitAll())
                 .csrf(csrf -> csrf.disable())
                 .formLogin(Customizer.withDefaults());
         return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile("dev")
    UserDetailsService userService(PasswordEncoder passwordEncoder, DataSource dataSource) {
        User.UserBuilder users = User.builder();
        UserDetails sarah = users
            .username("sarah2")
            .password(passwordEncoder.encode("abc123"))
            .roles("PAID", "ADMIN")
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
        JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);
        userManager.createUser(sarah);
        userManager.createUser(melissa);
        userManager.createUser(rory);
        return userManager;
    }

}