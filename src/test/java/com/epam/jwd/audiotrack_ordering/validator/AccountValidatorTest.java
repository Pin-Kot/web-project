package com.epam.jwd.audiotrack_ordering.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class AccountValidatorTest {

    private AccountValidator accountValidator;

    @BeforeEach
    public void before() {
        accountValidator = AccountValidator.getInstance();
    }

    @Test
    public void testIsLoginValid() {
        assertTrue(accountValidator.isLoginValid("user123"));
        assertFalse(accountValidator.isLoginValid("us")); // Short login
        assertFalse(accountValidator.isLoginValid("user@")); // Invalid characters
    }

    @Test
    public void testIsPasswordValid() {
        assertTrue(accountValidator.isPasswordValid("password123"));
        assertFalse(accountValidator.isPasswordValid("pas")); // Short password
        assertFalse(accountValidator.isPasswordValid("password@!")); // Invalid characters
    }

    @Test
    public void testIsAllValid() {
        assertTrue(accountValidator.isAllValid("user123", "password123"));
        assertFalse(accountValidator.isAllValid("us", "pass")); // Both invalid
        assertFalse(accountValidator.isAllValid("user123", "pas")); // Invalid password
        assertFalse(accountValidator.isAllValid("us", "password123")); // Invalid login
    }
}
