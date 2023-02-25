package com.epam.jwd.audiotrack_ordering.entity.dto;

import java.io.Serializable;

public interface Link extends Serializable {

    Long getLeftEntityId();

    Long getRightEntityId();
}
