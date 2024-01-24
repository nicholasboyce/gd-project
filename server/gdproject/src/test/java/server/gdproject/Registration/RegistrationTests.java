package server.gdproject.Registration;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import server.gdproject.TestSecurity.TestSecurityConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class RegistrationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DirtiesContext
    void shouldCreateANewUserAvailableForAuthentication() throws Exception {

        this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", "bill72")
                    .param("password", "banjokazooie"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(is(equalTo("Registration complete!"))));

        this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", "bill72")
                    .param("password", "banjokazooie"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(is(equalTo("User already exists"))));


        this.mockMvc.perform(formLogin().user("bill72").password("banjokazooie"))
                    .andExpect(authenticated());

        this.mockMvc.perform(formLogin().user("BAD-USER").password("banjokazooie"))
                    .andExpect(unauthenticated());

        this.mockMvc.perform(formLogin().user("bill72").password("BAD-PASSWORD"))
                    .andExpect(unauthenticated());
    }

}