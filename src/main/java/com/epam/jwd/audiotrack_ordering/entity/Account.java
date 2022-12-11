package com.epam.jwd.audiotrack_ordering.entity;

import java.util.Objects;

import static com.epam.jwd.audiotrack_ordering.entity.Role.USER;

public class Account implements Entity {

    private static final long serialVersionUID = -8704655277160314746L;

    private final Long id;
    private final String login;
    private final String password;
    private final Role role;

    public Account(Long id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Account(Long id, String login, String password) {
        this(id, login, password, USER);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Account withPassword(String password) {
        return new Account(id, login, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(login, account.login) &&
                Objects.equals(password, account.password) &&
                role == account.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
