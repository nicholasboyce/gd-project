package server.gdproject;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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


		// ResponseEntity<String> response = restTemplate
		// .withBasicAuth("sarah1", "abc123").getForEntity("/landrecords?page=0&size=1", String.class);
		// assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// DocumentContext documentContext = JsonPath.parse(response.getBody());
		// JSONArray page = documentContext.read("[*]");
		// assertThat(page.size()).isEqualTo(1);
	}

	@Test
	@WithMockUser(username = "sarah1", password = "abc123", roles = {"ADMIN", "PAID"})
	void shouldReturnASortedPageOfLandRecords() throws Exception {

		this.mockMvc.perform(get("/landrecords?page=0&size=1&sort=value,desc"))
					.andExpect(jsonPath("$.size()").value(1))
					.andExpect(jsonPath("$[0].value").value(3000000));

		// ResponseEntity<String> response = restTemplate
		// .withBasicAuth("sarah1", "abc123").getForEntity("/landrecords?page=0&size=1&sort=value,desc", String.class);
		// assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// DocumentContext documentContext = JsonPath.parse(response.getBody());
		// JSONArray page = documentContext.read("[*]");
		// assertThat(page.size()).isEqualTo(1);

		// int value = documentContext.read("$[0].value");
		// assertThat(value).isEqualTo(3000000);
	}

	// @Test
	// void shouldNotReturnACashCardWhenUsingBadCredentials() {

	// 	RequestPostProcessor badActor = user("BAD-USER").password("abc123");

	// 	@WithMockUser(username = "BAD-USER", password = "abc123")
	// 	this.mockMvc.perform(get("/landrecords/22").with())
	// 				.andExpect(status().isUnauthorized());
		
	// 	@WithMockUser(username = "sarah1", password = "BAD-PASSWORD")
	// 	this.mockMvc.perform(get("/landrecords/22").with())
	// 				.andExpect(status().isUnauthorized());

		// ResponseEntity<String> response = restTemplate
		// 	.withBasicAuth("BAD-USER", "abc123")
		// 	.getForEntity("/landrecords/22", String.class);
		// assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

		// response = restTemplate
		// 	.withBasicAuth("sarah1", "BAD-PASSWORD")
		// 	.getForEntity("/landrecords/22", String.class);
		// assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	

	@Test
	@DirtiesContext
	@WithMockUser(username = "melissa2", password = "xyz321", roles = "PAID")
	void shouldPreventUsersWhoAreNotAdminsFromCreatingRecords() throws Exception {
		LandRecord landRecord = new LandRecord("234 Ocean Way", "Kent Clark", 1982, 250000, 4, null);

		ObjectMapper mapper = new ObjectMapper();

		String body = mapper.writeValueAsString(landRecord);

		this.mockMvc.perform(post("/landrecords").content(body).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());

		// ResponseEntity<Void> response = restTemplate
		// 	.withBasicAuth("melissa2", "xyz321")
		// 	.postForEntity("/landrecords", landRecord, Void.class);

		// assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@WithMockUser(username = "roryg", password = "gilmore", roles = {"NON-PAID"})
	void shouldRejectUsersWhoAreNotPaidUsers() throws Exception{

		this.mockMvc.perform(get("/landrecords/22"))
					.andExpect(status().isForbidden());

		// ResponseEntity<String> response = restTemplate
		// 	.withBasicAuth("roryg", "gilmore")
		// 	.getForEntity("/landrecords/22", String.class);

		// assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
}
