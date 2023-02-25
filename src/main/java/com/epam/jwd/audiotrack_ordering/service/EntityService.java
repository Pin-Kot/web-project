package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Entity;

import java.util.List;

public interface EntityService<T extends Entity> {

    void create(T entity);

    List<T> findAll();

    void update(T entity);

    boolean delete(Long id);
}
