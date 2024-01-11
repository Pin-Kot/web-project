package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Card;

import java.math.BigDecimal;
import java.util.Optional;

public interface CardService extends EntityService<Card> {

    Optional<Card> findCardByNumber(String number);

    boolean transferMoney(Card from, Card to, BigDecimal amount);
}
