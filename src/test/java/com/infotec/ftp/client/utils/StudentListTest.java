package com.infotec.ftp.client.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StudentListTest {

    String jsonString = "{\n" +
            "    \"students\": [\n" +
            "        {\n" +
            "            \"id\": 1,\n" +
            "            \"name\": \"Student1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 2,\n" +
            "            \"name\": \"Student2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 3,\n" +
            "            \"name\": \"Student3\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 4,\n" +
            "            \"name\": \"Student4\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 5,\n" +
            "            \"name\": \"Student5\"\n" +
            "        }" +
            "    ]" +
            "}";

    public static boolean compareJson(String json1, String json2) {
        // Convert JSON strings to JSONObject
        JSONObject jsonObj1 = new JSONObject(json1);
        JSONObject jsonObj2 = new JSONObject(json2);

        // Compare the two JSON objects
        return compareJsonObjects(jsonObj1, jsonObj2);
    }

    private static boolean compareJsonObjects(JSONObject json1, JSONObject json2) {
        // Check if both are null or equal
        if (json1 == null && json2 == null) return true;
        if (json1 == null || json2 == null) return false;

        // Check keys and values
        Iterator<String> keys = json1.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!json2.has(key)) return false; // key not found in json2
            Object value1 = json1.get(key);
            Object value2 = json2.get(key);

            // If both values are JSONObjects, compare them recursively
            if (value1 instanceof JSONObject && value2 instanceof JSONObject) {
                if (!compareJsonObjects((JSONObject) value1, (JSONObject) value2)) {
                    return false;
                }
            } else if (value1 instanceof JSONArray && value2 instanceof JSONArray) {
                // Brain rot
                boolean flagOfIteration = false;
                for (Object o1 : ((JSONArray) value1)) {
                    for (Object o2 : ((JSONArray) value1)) {
                        if (compareJsonObjects((JSONObject) o1, (JSONObject) o2)) {
                            flagOfIteration = true;
                            break;
                        }
                    }
                }
            } else {
                // Compare values directly
                if (!value1.equals(value2)) {
                    return false;
                }
            }
        }

        // Check for additional keys in json2
        Iterator<String> keys2 = json2.keys();
        while (keys2.hasNext()) {
            String key = keys2.next();
            if (!json1.has(key)) return false; // key not found in json1
        }

        return true; // All keys and values match
    }

    @Test
    public void testFromJsonString() throws ParseException {
        StudentList studentList = StudentList.fromJsonString(jsonString);
        Assert.assertEquals(studentList.getStudentsList().size(), 5);

        List<Map.Entry<Integer, String>> students = studentList.getStudentsListWithId();
        Iterator<Map.Entry<Integer, String>> it = students.iterator();
        for (int i = 0; i < 5; i++) {
            Map.Entry<Integer, String> student = it.next();
            Assert.assertEquals(i + 1, (int) student.getKey());
            Assert.assertEquals(student.getValue(), "Student" + (i + 1));
        }
    }

    @Test
    public void testToJson() throws ParseException, IOException {
        StudentList studentList = StudentList.fromJsonString(jsonString);
        String json = studentList.toJson();
        Assert.assertTrue(compareJson(json, jsonString));
    }

    @Test
    public void testToJsonBackAndForth() throws IOException, ParseException {
        StudentList studentList = StudentList.fromJsonString(jsonString);
        String json = studentList.toJson();
        StudentList studentList2 = StudentList.fromJsonString(json);

        // Both sorted, so OK
        Assert.assertEquals(studentList2.getStudentsListWithId(), studentList.getStudentsListWithId());
    }

    @Test
    public void testGetStudentsList() throws ParseException {
        StudentList studentList = StudentList.fromJsonString(jsonString);
        List<String> students = studentList.getStudentsList();
        Assert.assertEquals(students.size(), 5);


        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(students.get(i), "Student" + (i + 1));
        }

        for (int i = 0; i < 4; i++) {
            Assert.assertTrue(students.get(i).compareTo(students.get(i + 1)) <= 0);
        }

        studentList.addStudent("Student0");
        students = studentList.getStudentsList();

        Assert.assertEquals(students.size(), 6);


        for (int i = 0; i < 6; i++) {
            Assert.assertEquals(students.get(i), "Student" + (i));
        }

        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(students.get(i).compareTo(students.get(i + 1)) <= 0);
        }
    }

    @Test
    public void testGetStudentsListListWithId() throws ParseException {
        StudentList studentList = StudentList.fromJsonString(jsonString);
        List<Map.Entry<Integer, String>> students = studentList.getStudentsListWithId();
        Assert.assertEquals(students.size(), 5);


        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(students.get(i).getValue(), "Student" + (i + 1));
        }

        for (int i = 0; i < 4; i++) {
            Assert.assertTrue(students.get(i).getValue().compareTo(students.get(i + 1).getValue()) <= 0);
        }

        studentList.addStudent("Student0");
        students = studentList.getStudentsListWithId();

        Assert.assertEquals(students.size(), 6);


        for (int i = 0; i < 6; i++) {
            Assert.assertEquals(students.get(i).getValue(), "Student" + (i));
        }

        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(students.get(i).getValue().compareTo(students.get(i + 1).getValue()) <= 0);
        }
    }
}