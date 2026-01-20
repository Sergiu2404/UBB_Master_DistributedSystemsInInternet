package com.example.patterns.customannotations;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectToJsonConverterUnitTest {

    @Test
    public void givenObjectNotSerializedThenExceptionThrown() throws JsonSerializationException {
        Object object = new Object();
        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        assertThrows(JsonSerializationException.class, () -> {
            serializer.convertToJson(object);
        });
    }

    @Test
    public void givenObjectSerializedThenTrueReturned() throws JsonSerializationException {
        Person person = new Person("john", "doe", "34");
        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        String jsonString = serializer.convertToJson(person);
        assertEquals("{\"personAge\":\"34\",\"firstName\":\"John\",\"lastName\":\"Doe\"}", jsonString);
    }

    @Test
    public void testAccess() {
        Person person = new Person("john", "doe", "34");
        Class<?> clazz = person.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
        }
        System.out.println("bla");
    }
}
