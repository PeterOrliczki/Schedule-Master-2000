//package com.codecool.web.model;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ScheduleTest {
//
//    private Schedule schedule;
//    private Schedule schedule2;
//    private Schedule schedule3;
//
//    @BeforeEach
//    void setUp() {
//        schedule = new Schedule(1, 1, "test title", 1);
//    }
//
//    @BeforeEach
//    void setUp2() {
//        schedule2 = new Schedule(2, 2, "test title 2", 2, true);
//    }
//
//    @BeforeEach
//    void setUp3() {
//        schedule3 = new Schedule(3, 3, "test title 3", 3);
//        schedule3.setVisibility(true);
//    }
//
//    @AfterEach
//    void tearDown() {
//        schedule = null;
//    }
//
//    @AfterEach
//    void tearDown2() {
//        schedule2 = null;
//    }
//
//    @AfterEach
//    void tearDown3() {
//        schedule3 = null;
//    }
//
//    @Test
//    void getUserId() {
//        assertEquals(1, schedule.getUserId());
//    }
//
//    @Test
//    void getTitle() {
//        assertEquals("test title", schedule.getTitle());
//    }
//
//    @Test
//    void getDuration() {
//        assertEquals(1, schedule.getDuration());
//    }
//
//    @Test
//    void isVisibility() {
//        assertFalse(schedule.isVisibility());
//    }
//
//    @Test
//    void isVisibility2() {
//        assertTrue(schedule2.isVisibility());
//    }
//
//    @Test
//    void setVisibility() {
//        assertTrue(schedule3.isVisibility());
//    }
//}
