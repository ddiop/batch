package hello.csv;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import hello.PersonItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvCreator1 {
    private static final Logger log = LoggerFactory.getLogger(CsvCreator1.class);

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        // POJO (bean class)
        @JsonPropertyOrder({"name", "age"})
        class User {
            public String name;
            public int age;

            public User() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }
        }

        // define objects
        User user1 = new User();
        user1.name = "foo";
        user1.age = 32;
        User user2 = new User();
        user2.name = "bar";
        user2.age = 27;
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        // Schema from POJO (usually has @JsonPropertyOrder annotation)
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(User.class);
        schema = schema.withColumnSeparator(',');
/*
// Manually-built schema: one with type, others default to "STRING"
        CsvSchema schema = CsvSchema.builder()
                .addColumn("firstName")
                .addColumn("lastName")
                .addColumn("age", CsvSchema.ColumnType.NUMBER)
                .build();
*/

        String csv = mapper.writer(schema).writeValueAsString(users);
        MappingIterator<User> it = mapper.readerFor(User.class).with(schema)
                .readValues(csv);

// or, alternatively all in one go
        List<User> all = it.readAll();
        log.info("csv {}",csv);
    }
    // https://github.com/FasterXML/jackson-dataformats-text/tree/master/csv
}