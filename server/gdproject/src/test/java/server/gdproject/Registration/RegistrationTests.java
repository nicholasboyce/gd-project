package server.gdproject.Registration;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import server.gdproject.TestSecurity.TestSecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.hamcrest.Matchers;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class RegistrationTests {

    @Autowired
    MockMvc mockMvc;

    // @Test
    // @DirtiesContext
    // void shouldCreateANewUserAvailableForAuthentication() {
    //     RegistrationRequest request = new RegistrationRequest("bill72", "banjokazooie");

    //     this.mockMvc.perform(post("/register").content(null))
    // }

}
