package com.epam.jwd.audiotrack_ordering.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UserValidatorTest {
    private UserValidator userValidator;

    @BeforeEach
    public void before() {
        userValidator = UserValidator.getInstance();
    }

    @Test
    void testIsFirstNameValid() {
        assertTrue(userValidator.isFirstNameValid("Xi"));//Valid first name
        assertFalse(userValidator.isFirstNameValid(null));//Null first name
        assertFalse(userValidator.isFirstNameValid(""));//Empty first name
        assertFalse(userValidator.isFirstNameValid("This is a very long name that is definitely longer than 150 " +
                "characters. This is a very long name that is definitely longer than 150 characters. This is a very " +
                "long name that is definitely longer than 150 characters.This is a very long name that is definitely " +
                "longer than 150 characters"));//Name longer than 150 characters
        assertFalse(userValidator.isFirstNameValid(" Invalid User"));//Name starting with whitespace
    }

    @Test
    void testIsLastNameValid() {
        assertTrue(userValidator.isFirstNameValid("Lee"));//Valid last name
        assertFalse(userValidator.isFirstNameValid(null));//Null last name
        assertFalse(userValidator.isFirstNameValid(""));//Empty last name
        assertFalse(userValidator.isFirstNameValid("This is a very long last name that is definitely longer than 150 " +
                "characters. This is a very long last name that is definitely longer than 150 characters. This is a " +
                "very long last name that is definitely longer than 150 characters.This is a very long last name " +
                "that is definitely longer than 150 characters"));//Last name longer than 150 characters
        assertFalse(userValidator.isFirstNameValid(" Invalid Surname"));//Last name starting with whitespace
    }

    @Test
    void testIsEmailValid() {
        assertTrue(userValidator.isEmailValid("user11@gmail.com"));//Valid email
        assertFalse(userValidator.isEmailValid(null));//Null email
        assertFalse(userValidator.isEmailValid(""));//Empty email
        assertFalse(userValidator.isEmailValid("user@mail.de"));//Invalid email
        assertFalse(userValidator.isEmailValid("userfromemail.be"));//Invalid email
        assertFalse(userValidator.isEmailValid(" user1718@inbox.com"));//Email starting with whitespace
    }

    @Test
    void testIsLeapYear() {
        assertTrue(userValidator.isLeapYear(2000));//Valid leap year
        assertTrue(userValidator.isLeapYear(2012));//Valid leap year
        assertFalse(userValidator.isLeapYear(1100));//Invalid leap year
        assertFalse(userValidator.isLeapYear(1900));//Invalid leap year
    }

    @Test
    void testIsNumeric() {
        assertTrue(userValidator.isNumeric("1983"));//Valid number
        assertFalse(userValidator.isNumeric(null));//Null number
        assertFalse(userValidator.isNumeric(""));//Empty number
        assertFalse(userValidator.isNumeric("1x2"));//Invalid number
    }

    @Test
    void testYearValid() {
        assertTrue(userValidator.isYearValid(2023));//Valid year
        assertTrue(userValidator.isYearValid(1900));//Valid year
        assertFalse(userValidator.isYearValid(1899));//Invalid year
        assertFalse(userValidator.isYearValid(2025));//Invalid year
    }

    @Test
    void testMonthValid() {
        assertTrue(userValidator.isMonthValid(3));//Valid month
        assertFalse(userValidator.isMonthValid(0));//Invalid month
        assertFalse(userValidator.isMonthValid(13));//Invalid month
    }

    @Test
    void testDayValid() {
        assertTrue(userValidator.isDayValid(2015, 8, 13));//Day valid
        assertFalse(userValidator.isDayValid(1990, 11, 31));//Day invalid
        assertFalse(userValidator.isDayValid(1977, 2, 29));//Day invalid
    }

    @Test
    void testDateValid() {
        assertTrue(userValidator.isDateValid(2015, 8, 13));//All valid
        assertFalse(userValidator.isDateValid(1966, 2, 29));//Day invalid
        assertFalse(userValidator.isDateValid(1999, 13, 29));//Month invalid
        assertFalse(userValidator.isDateValid(2024, 1, 1));//Year invalid
        assertFalse(userValidator.isDateValid(2024, 0, 41));//All invalid
    }

    @Test
    void testUserDataValid() {
        assertTrue(userValidator.isUserDataValid("User", "Test","user11@gmail.com",2015,
                8, 13));//All valid
        assertFalse(userValidator.isUserDataValid("u", " surname","usergmail.com",2024,
                0, 41));//All invalid
    }
}
