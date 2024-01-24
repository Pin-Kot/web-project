package com.epam.jwd.audiotrack_ordering.service.impl;

import com.epam.jwd.audiotrack_ordering.dao.CardDao;
import com.epam.jwd.audiotrack_ordering.entity.Card;
import com.epam.jwd.audiotrack_ordering.service.CardService;
import com.epam.jwd.audiotrack_ordering.service.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleCardService implements CardService {

    private final CardDao cardDao;
    private final ReentrantLock lock;

    SimpleCardService(CardDao cardDao) {
        this.cardDao = cardDao;
        this.lock = new ReentrantLock();
    }

    @Override
    public Optional<Card> findCardByNumber(String number) {
        return cardDao.findCardByNumber(number);
    }

    @Override
    @Transactional
    public boolean transferMoney(Card from, Card to, BigDecimal amount) {
        lock.lock();
        try {
            BigDecimal amountFrom = from.getAmount();
            BigDecimal amountTo = to.getAmount();
            if (amountFrom.compareTo(amount) == 1) {
                amountFrom = amountFrom.subtract(amount);
                amountTo = amountTo.add(amount);
                cardDao.updateCardRecipientAmount(amountFrom, from.getNumber());
                cardDao.updateCardRecipientAmount(amountTo, to.getNumber());
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void create(Card entity) {
    }

    @Override
    public Optional<Card> find(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() {
        return null;
    }

    @Override
    public void update(Card entity) {
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
