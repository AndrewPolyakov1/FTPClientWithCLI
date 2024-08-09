package com.infotec.ftp.client.utils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StudentList implements Iterable<Map.Entry<Integer, String>> {

    private final Map<Integer, String> studentMap;

    public StudentList() {
        studentMap = new HashMap<>();
    }

    public static StudentList fromJsonString(String jsonString) throws ParseException {
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            // Read and parse the JSON string
            JsonObject jsonObject = reader.readObject();
            JsonArray studentsArray = jsonObject.getJsonArray("students");
            StudentList studentList = new StudentList();

            // Iterate through the JSON array and add student details to the list
            for (JsonObject student : studentsArray.getValuesAs(JsonObject.class)) {
                int id = student.getInt("id");
                String name = student.getString("name");
                studentList.addStudent(id, name);
            }
            return studentList;

        } catch (Exception e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    public void addStudent(int id, String name) {
        studentMap.put(id, name);
    }

    public void addStudent(String name) {
        int id = studentMap.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        addStudent(id, name);
    }

    public String getById(int id) {
        return studentMap.get(id);
    }

    public void deleteById(int id) {
        studentMap.remove(id);
    }


    @Override
    public Iterator<Map.Entry<Integer, String>> iterator() {
        return studentMap.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<Integer, String>> action) {
        studentMap.entrySet().forEach(action);
    }

    @Override
    public Spliterator<Map.Entry<Integer, String>> spliterator() {
        return studentMap.entrySet().spliterator();
    }

    public String toJson() throws IOException {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        // Iterate over each entry in the map
        for (Map.Entry<Integer, String> entry : studentMap.entrySet()) {
            JsonObjectBuilder studentBuilder = Json.createObjectBuilder();
            studentBuilder.add("id", entry.getKey());
            studentBuilder.add("name", entry.getValue());

            jsonArrayBuilder.add(studentBuilder);
        }

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("students", jsonArrayBuilder)
                .build();

        // Create a configuration object to enable pretty printing
        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);


        // Convert the JsonObject to a JSON string
        try (StringWriter stringWriter = new StringWriter();
             JsonWriter jsonWriter = Json.createWriterFactory(config).createWriter(stringWriter)) {

            jsonWriter.writeObject(jsonObject);
            return stringWriter.toString();
        }
    }

    public List<String> getStudentsList() {
        return studentMap.values().stream().sorted().collect(Collectors.toList());
    }

    public List<Map.Entry<Integer, String>> getStudentsListWithId() {
        return studentMap.entrySet()
                .stream()
                .sorted((x, y) -> (String.CASE_INSENSITIVE_ORDER.compare(x.getValue(), y.getValue())))
                .collect(Collectors.toList());
    }
}
