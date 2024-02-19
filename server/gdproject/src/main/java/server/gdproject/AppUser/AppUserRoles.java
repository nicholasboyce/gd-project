package server.gdproject.AppUser;

import org.springframework.data.annotation.Id;

public record AppUserRoles(@Id Long id, String role, String email) {
}
