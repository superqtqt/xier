package io.github.superqtqt.aider.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;

/**
 * @author superqtqt 2021-06-06
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonAider {
    private static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public static <T> T readValue(JsonParser p, Class<T> valueType) {
        return MAPPER.readValue(p, valueType);
    }

    @SneakyThrows
    public static <T> T readValue(JsonParser p, TypeReference<T> valueTypeRef) {
        return MAPPER.readValue(p, valueTypeRef);
    }

    @SneakyThrows
    public static <T> T readValue(JsonParser p, JavaType valueType) {
        return MAPPER.readValue(p, valueType);
    }

    @SneakyThrows
    public static <T extends TreeNode> T readTree(JsonParser p) {
        return MAPPER.readTree(p);
    }

    @SneakyThrows
    public static <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType) {
        return MAPPER.readValues(p, valueType);
    }

    @SneakyThrows
    public static <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType) {
        return MAPPER.readValues(p, valueType);
    }

    @SneakyThrows
    public static <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType) {
        return MAPPER.readValues(p, valueType);
    }

    @SneakyThrows
    public static <T> MappingIterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) {
        return MAPPER.readValues(p, valueTypeRef);
    }

    @SneakyThrows
    public static JsonNode readTree(InputStream in) {
        return MAPPER.readTree(in);
    }

    @SneakyThrows
    public static JsonNode readTree(Reader r) {
        return MAPPER.readTree(r);
    }

    @SneakyThrows
    public static JsonNode readTree(String content) {
        return MAPPER.readTree(content);
    }

    @SneakyThrows
    public static JsonNode readTree(byte[] content) {
        return MAPPER.readTree(content);
    }

    @SneakyThrows
    public static JsonNode readTree(byte[] content, int offset, int len) {
        return MAPPER.readTree(content, offset, len);
    }

    @SneakyThrows
    public static JsonNode readTree(File file) {
        return MAPPER.readTree(file);
    }

    @SneakyThrows
    public static JsonNode readTree(URL source) {
        return MAPPER.readTree(source);
    }

    @SneakyThrows
    public static void writeValue(File resultFile, Object value) {
        MAPPER.writeValue(resultFile, value);
    }

    @SneakyThrows
    public static void writeValue(OutputStream out, Object value) {
        MAPPER.writeValue(out, value);
    }

    @SneakyThrows
    public static void writeValue(DataOutput out, Object value) {
        MAPPER.writeValue(out, value);
    }

    @SneakyThrows
    public static void writeValue(Writer w, Object value) {
        MAPPER.writeValue(w, value);
    }

    @SneakyThrows
    public static String writeValueAsString(Object value) {
        return MAPPER.writeValueAsString(value);
    }

    @SneakyThrows
    public static byte[] writeValueAsBytes(Object value) {
        return MAPPER.writeValueAsBytes(value);
    }
}
