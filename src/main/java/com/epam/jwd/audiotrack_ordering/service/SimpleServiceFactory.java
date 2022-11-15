package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.AccountDao;
import com.epam.jwd.audiotrack_ordering.dao.AlbumDao;
import com.epam.jwd.audiotrack_ordering.dao.ArtistDao;
import com.epam.jwd.audiotrack_ordering.dao.UserDao;
import com.epam.jwd.audiotrack_ordering.entity.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SimpleServiceFactory implements ServiceFactory {

    private static final String SERVICE_NOT_FOUND = "Could not create service for %s class";
    private final Map<Class<?>, EntityService<?>> serviceByEntity = new ConcurrentHashMap<>();

    private SimpleServiceFactory() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> EntityService<T> serviceFor(Class<T> modelClass) {
        return (EntityService<T>) serviceByEntity.computeIfAbsent(modelClass, createServiceFor());
    }

    private Function<Class<?>, EntityService<?>> createServiceFor() {
        return clazz -> {
            final String className = clazz.getSimpleName();
            switch (className) {
                case "Account":
                    return new SimpleAccountService(AccountDao.getInstance());
                case "Album":
                    return new AlbumService(AlbumDao.getInstance());
                case "Artist":
                    return new ArtistService(ArtistDao.getInstance());
                case "User":
                    return new UserService(UserDao.getInstance());
                default:
                    throw new IllegalArgumentException(String.format(SERVICE_NOT_FOUND, className));
            }
        };
    }

    static SimpleServiceFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final SimpleServiceFactory INSTANCE = new SimpleServiceFactory();
    }
}
