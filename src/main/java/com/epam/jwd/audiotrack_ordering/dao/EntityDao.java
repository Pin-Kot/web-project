package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity> {

    void create(T entity);

    Optional<T> find(Long id);

    List<T> findAll();

    void update(T entity);

    boolean delete(Long id);
}
