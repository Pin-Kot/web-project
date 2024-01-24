package com.epam.jwd.audiotrack_ordering.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class EnteredDataValidatorTest {
    private EnteredDataValidator enteredDataValidator;

    @BeforeEach
    public void before() {
        enteredDataValidator = EnteredDataValidator.getInstance();
    }

    @Test
    void testIsTitleValid() {
        assertTrue(enteredDataValidator.isTitleValid("Valid Title"));//Valid title
        assertFalse(enteredDataValidator.isTitleValid(null));//Null title
        assertFalse(enteredDataValidator.isTitleValid(""));//Empty title
        assertFalse(enteredDataValidator.isTitleValid("This is a very long title that is definitely longer than 250 " +
                "characters. This is a very long title that is definitely longer than 250 characters. This is a very " +
                "long title that is definitely longer than 250 characters.This is a very long title that is definitely " +
                "longer than 250 characters"));//Title longer than 250 characters
        assertFalse(enteredDataValidator.isTitleValid(" Invalid Title"));//Title starting with whitespace
    }

    @Test
    void testIsDecimal() {
        assertTrue(enteredDataValidator.isDecimal("123.45"));//Valid number
        assertFalse(enteredDataValidator.isDecimal("12A.45"));//Invalid number
        assertFalse(enteredDataValidator.isDecimal(""));//Empty number
        assertFalse(enteredDataValidator.isDecimal(null));//Null number
    }

    @Test
    void testIsTrackYearValid() {
        assertTrue(enteredDataValidator.isTrackYearValid("1999"));//Valid year
        assertFalse(enteredDataValidator.isTrackYearValid("1799"));//Invalid year
        assertFalse(enteredDataValidator.isTrackYearValid("2025"));//Invalid year
        assertFalse(enteredDataValidator.isTrackYearValid(""));//Empty year
        assertFalse(enteredDataValidator.isTrackYearValid(null));//Null year
    }

    @Test
    void testIsTypeValid() {
        assertTrue(enteredDataValidator.isTypeValid("STUDIO"));//Valid type
        assertFalse(enteredDataValidator.isTypeValid(null));//Null type
        assertFalse(enteredDataValidator.isTypeValid(""));//Empty type
        assertFalse(enteredDataValidator.isTypeValid("This is a very long type"));//Title longer than 250 characters
        assertFalse(enteredDataValidator.isTypeValid(" Invalid Title"));//Type starting with whitespace
        assertFalse(enteredDataValidator.isTypeValid("studio"));//Low case type
    }

    @Test
    void testIsDiscountValid() {
        assertTrue(enteredDataValidator.isDiscountValid("0"));//Valid discount
        assertTrue(enteredDataValidator.isDiscountValid("20"));//Valid discount
        assertFalse(enteredDataValidator.isDiscountValid("21"));//Invalid discount
        assertFalse(enteredDataValidator.isDiscountValid(""));//Empty discount
        assertFalse(enteredDataValidator.isDiscountValid(null));//Null discount
    }

    @Test
    void testIsLongNumberValid() {
        assertTrue(enteredDataValidator.isLongNumberValid("999999999999999999999999999999999990"));//Valid number
        assertFalse(enteredDataValidator.isLongNumberValid("12A45"));//Invalid number
        assertFalse(enteredDataValidator.isLongNumberValid(""));//Empty number
        assertFalse(enteredDataValidator.isLongNumberValid(null));//Null number
    }

    @Test
    void testIsAlbumDataValid() {
        assertTrue(enteredDataValidator.isAlbumDataValid("Good title", "2022", "STUDIO"));//All valid
        assertFalse(enteredDataValidator.isAlbumDataValid("ABC", "2025", "studio"));//All invalid
    }

    @Test
    void testIsTrackDataValid() {
        assertTrue(enteredDataValidator.isTrackDataValid("Valid title", "2011", "1.11"));//All valid
        assertFalse(enteredDataValidator.isTrackDataValid("tit", "1660", "11d5"));//All invalid
    }

    @Test
    void testIsTextReviewValid() {
        assertTrue(enteredDataValidator.isTextReviewValid("Very good, valid review"));//valid review
        assertFalse(enteredDataValidator.isTextReviewValid(" Wrong review"));//invalid review
        assertFalse(enteredDataValidator.isTextReviewValid("Very loooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong " +
                "review"));//invalid review
    }
}
