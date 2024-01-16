package server.gdproject;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@JsonTest
public class landRecordJSONTest {

    @Autowired
    private JacksonTester<LandRecord> json;

    @Autowired
    private JacksonTester<LandRecord[]> jsonList;
    private LandRecord[] landRecords;

    @BeforeEach
    void SetUp() {
        landRecords = Arrays.array(
            new LandRecord("123 Mt Gay Rd", "Michael Covers", 2023, 100000, 1, 22L),
            new LandRecord("15 Governor's Road", "John Henry", 2018, 1000000, 2, 23L),
            new LandRecord("42 Nutmeg Hill", "Joseph Covers", 2007, 700000, 3, 30L),
            new LandRecord("1 Braithwaite Estate", "Anthony Brathwaite", 2013, 3000000, 4, 14L)
        );
    }

    @Test
    void landRecordSerializationTest() throws IOException {

        LandRecord landRecord = landRecords[0];

        assertThat(json.write(landRecord)).isStrictlyEqualToJson("single.json");

        assertThat(json.write(landRecord)).hasJsonPathStringValue("@.address");

        assertThat(json.write(landRecord)).extractingJsonPathStringValue("@.address").isEqualTo("123 Mt Gay Rd");

        assertThat(json.write(landRecord)).hasJsonPathStringValue("@.owner");

        assertThat(json.write(landRecord)).extractingJsonPathStringValue("@.owner").isEqualTo("Michael Covers");

        assertThat(json.write(landRecord)).hasJsonPathNumberValue("@.year");

        assertThat(json.write(landRecord)).extractingJsonPathNumberValue("@.year").isEqualTo(2023);

        assertThat(json.write(landRecord)).hasJsonPathNumberValue("@.value");

        assertThat(json.write(landRecord)).extractingJsonPathNumberValue("@.value").isEqualTo(100000);

        assertThat(json.write(landRecord)).hasJsonPathNumberValue("@.book");

        assertThat(json.write(landRecord)).extractingJsonPathNumberValue("@.book").isEqualTo(1);

        assertThat(json.write(landRecord)).hasJsonPathNumberValue("@.id");

        assertThat(json.write(landRecord)).extractingJsonPathNumberValue("@.id").isEqualTo(22);
    }

    @Test
    void landRecordDeserializationTest() throws IOException {
        String expected = """
                {
                    "year": 2023,
                    "book": 1,
                    "address": "123 Mt Gay Rd",
                    "value": 100000,
                    "owner": "Michael Covers",
                    "id": 22
                }
                """;
                
        assertThat(json.parse(expected)).isEqualTo(new LandRecord("123 Mt Gay Rd", "Michael Covers", 2023, 100000, 1, 22L));

        assertThat(json.parseObject(expected).address()).isEqualTo("123 Mt Gay Rd");

        assertThat(json.parseObject(expected).owner()).isEqualTo("Michael Covers");

        assertThat(json.parseObject(expected).value()).isEqualTo(100000);

        assertThat(json.parseObject(expected).year()).isEqualTo(2023);

        assertThat(json.parseObject(expected).book()).isEqualTo(1);

        assertThat(json.parseObject(expected).id()).isEqualTo(22);
    }

    @Test
    void landRecordListSerializationTest() throws IOException {
        assertThat(jsonList.write(landRecords)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void landRecordListDeserializationTest() throws IOException {
        String expected = """
            [
                {
                    "year": 2023,
                    "book": 1,
                    "address": "123 Mt Gay Rd",
                    "value": 100000,
                    "owner": "Michael Covers",
                    "id": 22
                },
                {
                    "year": 2018,
                    "book": 2,
                    "id": 23, 
                    "address": "15 Governor's Road",
                    "value": 1000000,
                    "owner": "John Henry"
                },
                {   
                    "year": 2007,
                    "book": 3,
                    "id": 30, 
                    "address": "42 Nutmeg Hill",
                    "value": 700000,
                    "owner": "Joseph Covers"
                },
                {
                    "year": 2013,
                    "book": 4,
                    "id": 14, 
                    "address": "1 Braithwaite Estate",
                    "value": 3000000,
                    "owner": "Anthony Brathwaite"
                }
            ]
                """;

        assertThat(jsonList.parse(expected)).isEqualTo(landRecords);
    }
 
}
