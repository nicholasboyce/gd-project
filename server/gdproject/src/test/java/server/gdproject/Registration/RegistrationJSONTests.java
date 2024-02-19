package server.gdproject.Registration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class RegistrationJSONTests {

    @Autowired
    JacksonTester<RegistrationRequest> json;


    @Test
    void registrationRequestSerializationTest() throws Exception {

        RegistrationRequest request = new RegistrationRequest("BigCo", "Industry", "bill", "bob", "billbob@bibby.com", "greenelephant", "111 Big Hill", "Mt Gay", 11111, "GD", "5550555", "5550155", "Analyst");

        assertThat(json.write(request)).isStrictlyEqualToJson("../registrationRequest.json");

        assertThat(json.write(request)).hasJsonPathStringValue("@.email");
        assertThat(json.write(request)).extractingJsonPathStringValue("@.email").isEqualTo("billbob@bibby.com");

        assertThat(json.write(request)).hasJsonPathStringValue("@.password");
        assertThat(json.write(request)).extractingJsonPathStringValue("@.password").isEqualTo("greenelephant");
    }

    @Test
    void registrationRequestDeserializationTest() throws Exception {

        String expected = """
            {
                "company": "BigCo",
                "businessType": "Industry",
                "firstName": "bill",
                "lastName": "bob",
                "email": "billbob@bibby.com",
                "password": "greenelephant",
                "address": "111 Big Hill",
                "city": "Mt Gay",
                "zip": 11111,
                "country": "GD",
                "home": "5550555",
                "mobile": "5550155",
                "job": "Analyst"
            }
                """;
        
        assertThat(json.parse(expected)).isEqualTo(new RegistrationRequest("BigCo", "Industry", "bill", "bob", "billbob@bibby.com", "greenelephant", "111 Big Hill", "Mt Gay", 11111, "GD", "5550555", "5550155", "Analyst"));
        assertThat(json.parseObject(expected).email()).isEqualTo("billbob@bibby.com");
        assertThat(json.parseObject(expected).password()).isEqualTo("greenelephant");
    }
}
