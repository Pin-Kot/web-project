package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Entity;

import java.util.List;

public interface EntityService<T extends Entity> {

    List<T> findAll();

    void create(T entity);

    void update(T entity);
}
