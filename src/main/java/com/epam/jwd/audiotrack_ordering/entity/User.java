package com.epam.jwd.audiotrack_ordering.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class User implements Entity {

    private static final long serialVersionUID = -6855337589302892701L;
    
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Date birthday;
    private final BigDecimal discount;
    private final Long accId;

    public User(Long id, String firstName, String lastName, String email, Date birthday,
                BigDecimal discount, Long accId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.discount = discount;
        this.accId = accId;
    }

    public User(String firstName, String lastName, String email, Date birthday,
                BigDecimal discount, Long accId) {
        this(null, firstName, lastName, email, birthday,
                discount, accId);
    }


    @Override
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public Long getAccId() {
        return accId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(birthday, user.birthday) &&
                Objects.equals(discount, user.discount) &&
                Objects.equals(accId, user.accId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, birthday, discount, accId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", discount=" + discount +
                ", accId=" + accId +
                '}';
    }
}
