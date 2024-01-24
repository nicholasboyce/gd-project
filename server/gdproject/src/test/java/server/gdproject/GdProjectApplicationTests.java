package server.gdproject;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;

import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import net.minidev.json.JSONArray;
import server.gdproject.Security.TestSecurityConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class GdProjectApplicationTests {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	@WithMockUser(username = "sarah1", password = "abc123", roles = {"ADMIN", "PAID"})
	void shouldReturnALandRecordWhenDataIsSaved() throws Exception {
		this.mockMvc.perform(get("/landrecords/22"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.address").value("123 Mulberry Lane"))
					.andExpect(jsonPath("$.owner").value("Nicholas Boyce"))
					.andExpect(jsonPath("$.year").value(2024))
					.andExpect(jsonPath("$.value").value(777000))
					.andExpect(jsonPath("$.book").value(1))
					.andExpect(jsonPath("$.id").value(22));
	}

	@Test
	@WithMockUser(username = "sarah1", password = "abc123", roles = {"ADMIN", "PAID"})
	void shouldNotReturnALandRecordWithAnUnknownID() throws Exception{
		this.mockMvc.perform(get("/landrecords/2222"))
					.andExpect(status().isNotFound())
					.andExpect(content().string(Matchers.blankOrNullString()));
	}

	@Test
	@DirtiesContext
	@WithMockUser(username = "sarah1", password = "abc123", roles = {"ADMIN", "PAID"})
	void shouldCreateANewLandRecord() throws Exception{
		LandRecord landRecord = new LandRecord("234 Ocean Way", "Kent Clark", 1982, 250000, 4, null);

		ObjectMapper mapper = new ObjectMapper();

		String body = mapper.writeValueAsString(landRecord);

		MvcResult result = this.mockMvc.perform(post("/landrecords").content(body).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated())
					.andReturn();

		String postURL = result.getResponse().getHeader("Location");
		
		this.mockMvc.perform(get(postURL))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").isNotEmpty())
					.andExpect(jsonPath("$.owner").value("Kent Clark"))
					.andExpect(jsonPath("$.address").value("234 Ocean Way"));
	}

	@Test
	@WithMockUser(username = "sarah1", password = "abc123", roles = {"ADMIN", "PAID"})
	void shouldReturnAllLandRecordsWhenListIsRequested() throws Exception {

		this.mockMvc.perform(get("/landrecords"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.length()").value(4))
					.andExpect(jsonPath("$..id").value(containsInAnyOrder(22, 23, 30, 14)))
					.andExpect(jsonPath("$..value").value(containsInAnyOrder(777000, 1000000, 700000, 3000000)));
	}

	@Test
	@WithMockUser(username = "sarah1", password = "abc123", roles = {"ADMIN", "PAID"})
	void shouldReturnAPageOfLandRecords() throws Exception {

		this.mockMvc.perform(get("/landrecords?page=0&size=1"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.size()").value(1));
	}

	@Test
	@WithMockUser(username = "sarah1", password = "abc123", roles = {"ADMIN", "PAID"})
	void shouldReturnASortedPageOfLandRecords() throws Exception {

		this.mockMvc.perform(get("/landrecords?page=0&size=1&sort=value,desc"))
					.andExpect(jsonPath("$.size()").value(1))
					.andExpect(jsonPath("$[0].value").value(3000000));

	}

	//Not really a land record test... more of an authentication/registration test
	@Test
	void shouldNotReturnALandRecordWhenUsingBadCredentials() throws Exception {

		this.mockMvc.perform(formLogin().user("sarah1").password("abc123"))
					.andExpect(authenticated());

		this.mockMvc.perform(formLogin().user("BAD-USER").password("abc123"))
					.andExpect(unauthenticated())
					.andExpect(status().isFound());
		
		this.mockMvc.perform(formLogin().user("sarah1").password("BAD-PASSWORD"))
					.andExpect(unauthenticated());
	}

	@Test
	@DirtiesContext
	@WithMockUser(username = "melissa2", password = "xyz321", roles = "PAID")
	void shouldPreventUsersWhoAreNotAdminsFromCreatingRecords() throws Exception {
		LandRecord landRecord = new LandRecord("234 Ocean Way", "Kent Clark", 1982, 250000, 4, null);

		ObjectMapper mapper = new ObjectMapper();

		String body = mapper.writeValueAsString(landRecord);

		this.mockMvc.perform(post("/landrecords").content(body).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "roryg", password = "gilmore", roles = {"NON-PAID"})
	void shouldRejectUsersWhoAreNotPaidUsers() throws Exception{

		this.mockMvc.perform(get("/landrecords/22"))
					.andExpect(status().isForbidden());
	}
}
