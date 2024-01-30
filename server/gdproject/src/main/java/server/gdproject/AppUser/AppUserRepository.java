package server.gdproject.AppUser;

import org.springframework.data.repository.CrudRepository;

interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByEmail(String email);
    boolean existsByEmail(String email);
}
