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
}
