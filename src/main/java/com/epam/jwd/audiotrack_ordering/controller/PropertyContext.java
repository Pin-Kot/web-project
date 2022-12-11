package com.epam.jwd.audiotrack_ordering.controller;

public interface PropertyContext {

    String get(String name);

    static PropertyContext getInstance() {
        return SimplePropertyContext.getInstance();
    }
}
