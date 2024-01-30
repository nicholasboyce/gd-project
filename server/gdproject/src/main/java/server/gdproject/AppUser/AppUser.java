package server.gdproject.AppUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record AppUser(
    @Id Long id, 
    String company, 
    String businessType, 
    String firstName, 
    String lastName, 
    String email,
    String password, 
    String address, 
    String city, 
    int zip, 
    String country, 
    String home, 
    String mobile, 
    String job,
    Collection<AppUserRoles> roles) implements UserDetails {


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AppUserRoles role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.role()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true;
    }

    public static AppUser builder(String email, String password) {
        List<AppUserRoles> roles = Collections.singletonList(new AppUserRoles(null, "PAID", email));
        return new AppUser(null, "Company", "Industry", "Ted", "Watterson", email, password, "123 Townsville Rd", "Townsville", 11111, "GD", "5555555", "5555555", "Caretaker", roles);
    }
    

}
