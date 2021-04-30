package ru.topjava.lunchvote.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class JsonConverter {

    private static final JsonConverter CONVERTER = new JsonConverter();
    private JsonConverter() {
    }

    @Autowired
    private ObjectMapper mapper;

    public <T> T readValueFromJson(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        String json = result.getResponse().getContentAsString();
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    public <T> List<T> readValuesFromJson(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        String json = result.getResponse().getContentAsString();
        ObjectReader reader = mapper.readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    public <T> String writeValue(T entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + entity + "'", e);
        }
    }

    public <T> String writeAdditionProperties(T entity, String addName, Object addValue) {
        Map<String, Object> map = mapper.convertValue(entity, new TypeReference<>() {
        });
        map.putAll(Map.of(addName, addValue));
        return writeValue(map);
    }

    public static JsonConverter getConverter() {
        return CONVERTER;
    }
}
