package swm.hkcc.consumer.app.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectJsonConverter implements DynamoDBTypeConverter<String, Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize to json", e);
        }
    }

    @Override
    public Object unconvert(String s) {
        try {
            return objectMapper.readValue(s, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to deserialize from json", e);
        }
    }
}

