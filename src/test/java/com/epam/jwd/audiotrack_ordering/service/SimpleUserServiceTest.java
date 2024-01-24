package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.impl.MethodUserDao;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.service.impl.SimpleUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleUserServiceTest {
    private MethodUserDao userDaoTest;
    private SimpleUserService userServiceTest;
    private User userTest;
    private User updateUserTest;
    private Long idTest;
    private String firstNameTest;
    private String lastNameTest;
    private String emailTest;
    private LocalDate birthdayTest;
    private BigDecimal discountTest;
    private Long accIdTest;
    private List<User> usersListTest;

    @BeforeAll
    void beforeAll(){
        userDaoTest = mock(MethodUserDao.class);
        userServiceTest = new SimpleUserService(userDaoTest);
        idTest = 1L;
        firstNameTest = "Sam";
        lastNameTest = "Rain";
        emailTest = "sam@inbox.com";
        birthdayTest = LocalDate.of(2001, 01, 01);
        discountTest = new BigDecimal(5);
        accIdTest = 2L;
        userTest = new User(idTest, firstNameTest, lastNameTest, emailTest, birthdayTest, discountTest, accIdTest);
        updateUserTest = new User(idTest, firstNameTest, lastNameTest, emailTest, birthdayTest, new BigDecimal(10),
                accIdTest);
        usersListTest = new ArrayList<>();
        usersListTest.add(userTest);
    }

    @Test
    void createTest() {
        userServiceTest.create(userTest);
        verify(userDaoTest).create(userTest);
    }

    @Test
    void findTest() {
        Optional<User> expectedResult = Optional.of(userTest);
        when(userDaoTest.find(idTest)).thenReturn(expectedResult);
        Optional<User> actualResult = userServiceTest.find(idTest);
        assertTrue(actualResult.isPresent());
        assertEquals(actualResult, expectedResult);
    }

    @Test
    void findAllTest() {
        List<User> expectedResult = usersListTest;
        when(userDaoTest.findAll()).thenReturn(expectedResult);
        List<User> actualResult = userServiceTest.findAll();
        assertEquals(actualResult, expectedResult);
    }

    @Test
    void findByAccountIdTest() {
        Optional<User> expectedResult = Optional.of(userTest);
        when(userDaoTest.findUserByAccountId(accIdTest)).thenReturn(expectedResult);
        Optional<User> actualResult = userServiceTest.findUserByAccountId(accIdTest);
        assertTrue(actualResult.isPresent());
        assertEquals(actualResult, expectedResult);
    }

    @Test
    void updateTest() {
        userServiceTest.update(updateUserTest);
        verify(userDaoTest).update(updateUserTest);
    }
}
