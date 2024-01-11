package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.dao.impl.MethodCardDao;
import com.epam.jwd.audiotrack_ordering.entity.Card;

import java.math.BigDecimal;
import java.util.Optional;

public interface CardDao extends EntityDao<Card> {

    Optional<Card> findCardByNumber(String number);

    boolean updateCardRecipientAmount(BigDecimal amount, String cardRecipientNumber);

    static CardDao getInstance() {
        return MethodCardDao.getInstance();
    }
}
