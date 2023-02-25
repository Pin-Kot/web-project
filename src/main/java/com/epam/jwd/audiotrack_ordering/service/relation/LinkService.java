package com.epam.jwd.audiotrack_ordering.service.relation;

import com.epam.jwd.audiotrack_ordering.entity.dto.Link;

import java.util.Optional;

public interface LinkService<T extends Link> {

    void create(T link);

    ///
    void createById(Long id);

    Optional<T> findLink(Long leftEntityId, Long rightEntityId);

    void delete(T link);
}
