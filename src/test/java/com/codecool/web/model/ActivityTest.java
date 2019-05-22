//package com.codecool.web.model;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class ActivityTest {
//
//    private Activity activity;
//
//    @BeforeEach
//    void setUp() {
//        activity = new Activity("test event name", "test table name", "test user name", "test event date");
//    }
//
//    @AfterEach
//    void tearDown() {
//        activity = null;
//    }
//
//    @Test
//    void getEventName() {
//        assertEquals("test event name", activity.getEventName());
//    }
//
//    @Test
//    void getTableName() {
//        assertEquals("test table name", activity.getTableName());
//    }
//
//    @Test
//    void getUserName() {
//        assertEquals("test user name", activity.getUserName());
//    }
//
//    @Test
//    void getEventDate() {
//        assertEquals("test event date", activity.getEventDate());
//    }
//}
