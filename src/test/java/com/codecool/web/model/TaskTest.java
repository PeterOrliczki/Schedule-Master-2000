//package com.codecool.web.model;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class TaskTest {
//
//    private Task task;
//    private Task task2;
//
//    @BeforeEach
//    void setUp() {
//        task = new Task(1, 1, "test title", "test content", 1, 2);
//    }
//
//    @BeforeEach
//    void setUp2() {
//        task2 = new Task(2, 2, "test title 2", "test content 2", 2, 3);
//        task2.setColumnNumber(2);
//    }
//
//    @AfterEach
//    void tearDown() {
//        task = null;
//    }
//
//    @AfterEach
//    void tearDown2() {
//        task2 = null;
//    }
//
//    @Test
//    void getUserId() {
//        assertEquals(1, task.getUserId());
//    }
//
//    @Test
//    void getTitle() {
//        assertEquals("test title", task.getTitle());
//    }
//
//    @Test
//    void getContent() {
//        assertEquals("test content", task.getContent());
//    }
//
//    @Test
//    void getStart() {
//        assertEquals(1, task.getStart());
//    }
//
//    @Test
//    void getEnd() {
//        assertEquals(2, task.getEnd());
//    }
//
//    @Test
//    void getColumnNumber() {
//        assertEquals(0, task.getColumnNumber());
//    }
//
//    @Test
//    void getColumnNumber2() {
//        assertEquals(2, task2.getColumnNumber());
//    }
//
//    @Test
//    void setColumnNumber() {
//        task.setColumnNumber(1);
//        assertEquals(1, task.getColumnNumber());
//    }
//}
