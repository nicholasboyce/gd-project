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

        RegistrationRequest request = new RegistrationRequest("greenelephant", "dumbo");

        assertThat(json.write(request)).isStrictlyEqualToJson("../registrationRequest.json");

        assertThat(json.write(request)).hasJsonPathStringValue("@.username");
        assertThat(json.write(request)).extractingJsonPathStringValue("@.username").isEqualTo("greenelephant");

        assertThat(json.write(request)).hasJsonPathStringValue("@.password");
        assertThat(json.write(request)).extractingJsonPathStringValue("@.password").isEqualTo("dumbo");
    }

    @Test
    void registrationRequestDeserializationTest() throws Exception {

        String expected = """
                {
                    "username": "greenelephant",
                    "password": "dumbo"
                }
                """;
        
        assertThat(json.parse(expected)).isEqualTo(new RegistrationRequest("greenelephant", "dumbo"));
        assertThat(json.parseObject(expected).username()).isEqualTo("greenelephant");
        assertThat(json.parseObject(expected).password()).isEqualTo("dumbo");
    }
}
