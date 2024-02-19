package server.gdproject.Login;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository(); 

	public LoginController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

    @GetMapping("/login")
	String login() {
		return "login";
	}

    @CrossOrigin(origins = {"https://nicholasboyce.github.io", "http://localhost:5173"})
    @PostMapping(
        path = "/login",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> postLogin(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username(), loginRequest.password()); 
        Authentication authentication = authenticationManager.authenticate(token); 
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication); 
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response); 
        return ResponseEntity.ok().body("Hooray!");
    }

    public record LoginRequest(String username, String password) {
	}

}
