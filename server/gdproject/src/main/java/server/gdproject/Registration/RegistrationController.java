package server.gdproject.Registration;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private RegistrationService registrationService;

    private RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @CrossOrigin(origins = {"https://nicholasboyce.github.io", "http://localhost:5173"})
    @PostMapping
    @ResponseBody
    private ResponseEntity<String> createUser(RegistrationRequest newRegistrationRequest) {
        return registrationService.register(newRegistrationRequest);
    }

    @GetMapping
    private String register() {
        return "register";
    }

}
