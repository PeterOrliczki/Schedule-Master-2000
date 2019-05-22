//package com.codecool.web.model;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class UserTest {
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        user = new User(1, "test name", "test email", "test password", Role.REGULAR);
//    }
//
//    @AfterEach
//    void tearDown() {
//        user = null;
//    }
//
//    @Test
//    void getName() {
//        assertEquals("test name", user.getName());
//    }
//
//    @Test
//    void getEmail() {
//        assertEquals("test email", user.getEmail());
//    }
//
//    @Test
//    void getPassword() {
//        assertEquals("test password", user.getPassword());
//    }
//
//    @Test
//    void getRole() {
//        assertEquals(Role.REGULAR, user.getRole());
//    }
//}
