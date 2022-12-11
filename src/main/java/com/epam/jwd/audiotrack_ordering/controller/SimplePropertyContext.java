package com.epam.jwd.audiotrack_ordering.controller;

public class SimplePropertyContext implements PropertyContext {

    private static final String PAGE_PATH_NOT_FOUND = "Could not find page path for %s";
    private static final String PROPERTY_PREFIX = "page.";

    private SimplePropertyContext() {
    }

    @Override
    public String get(String name) {
        if (name.startsWith(PROPERTY_PREFIX)) {
            return PagePaths.of(name.substring(PROPERTY_PREFIX.length())).getPath();
        }
        throw new IllegalArgumentException(String.format(PAGE_PATH_NOT_FOUND, name));
    }

    static SimplePropertyContext getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final SimplePropertyContext INSTANCE = new SimplePropertyContext();
    }
}
