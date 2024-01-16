package server.gdproject;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GdProjectApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnALandRecordWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.getForEntity("/landrecords/22", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		String address = documentContext.read("$.address");
		assertThat(address).isEqualTo("123 Mulberry Lane");

		String owner = documentContext.read("$.owner");
		assertThat(owner).isEqualTo("Nicholas Boyce");

		Number year = documentContext.read("$.year");
		assertThat(year).isEqualTo(2024);

		Number value = documentContext.read("$.value");
		assertThat(value).isEqualTo(777000);

		Number book = documentContext.read("$.book");
		assertThat(book).isEqualTo(1);

		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(22);
	}

	@Test
	void shouldNotReturnALandRecordWithAnUnknownID() {
		ResponseEntity<String> response = restTemplate.getForEntity("/landrecords/2222", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	@DirtiesContext
	void shouldCreateANewLandRecord() {
		LandRecord landRecord = new LandRecord("234 Ocean Way", "Kent Clark", 1982, 250000, 4, null);

		ResponseEntity<Void> createResponse = restTemplate
												.postForEntity("/landrecords", landRecord, Void.class);
		
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewLandRecord = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate
												.getForEntity(locationOfNewLandRecord, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

		Number id = documentContext.read("$.id");
		assertThat(id).isNotNull();

		String owner = documentContext.read("$.owner");
		assertThat(owner).isEqualTo("Kent Clark");

		String address = documentContext.read("$.address");
		assertThat(address).isEqualTo("234 Ocean Way");
	}

	@Test
	void shouldReturnAllLandRecordsWhenListIsRequested() {
		ResponseEntity<String> response = restTemplate
											.getForEntity("/landrecords", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int landRecordCount = documentContext.read("$.length()");
		assertThat(landRecordCount).isEqualTo(4);

		JSONArray ids = documentContext.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(22, 23, 30, 14);

		JSONArray values = documentContext.read("$..value");
		assertThat(values).containsExactlyInAnyOrder(777000, 1000000, 700000, 3000000);
	}

	@Test
	void shouldReturnAPageOfLandRecords() {
		ResponseEntity<String> response = restTemplate
											.getForEntity("/landrecords?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("[*]");
		assertThat(page.size()).isEqualTo(1);
	}

	@Test
	void shouldReturnASortedPageOfLandRecords() {
		ResponseEntity<String> response = restTemplate
											.getForEntity("/landrecords?page=0&size=1&sort=value,desc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("[*]");
		assertThat(page.size()).isEqualTo(1);

		int value = documentContext.read("$[0].value");
		assertThat(value).isEqualTo(3000000);
	}
}