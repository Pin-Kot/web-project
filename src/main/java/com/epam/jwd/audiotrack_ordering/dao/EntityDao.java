package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity> {

    T create(T entity);

    Optional<T> read(Long id);

    List<T> read();

    T update(T entity);

    boolean delete(Long id);
}
