package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityService<T extends Entity> {

    List<T> findAll();

    Optional<T> create(T entity);
}
