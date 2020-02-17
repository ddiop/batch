package hello.csv;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
class UserMessageConverter implements MessageConverter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserMessageConverter.class);

    ObjectMapper mapper;


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

    public UserMessageConverter() {
        mapper = new ObjectMapper();
    }

    @Override
    public Message toMessage(Object object, Session session)
            throws JMSException {
        User user = (User) object;
        String payload = null;
        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(User.class);
            schema = schema.withColumnSeparator(',');
           // payload = mapper.writeValueAsString(person);
            payload  = mapper.writer(schema).writeValueAsString(user);
            LOGGER.info("outbound json='{}'", payload);
        } catch (JsonProcessingException e) {
            LOGGER.error("error converting form person", e);
        }

        TextMessage message = session.createTextMessage();
        message.setText(payload);

        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String payload = textMessage.getText();
        LOGGER.info("inbound json='{}'", payload);

        User user = null;
        try {
            user = mapper.readValue(payload, User.class);
        } catch (Exception e) {
            LOGGER.error("error converting to user", e);
        }

        return user;
    }
}