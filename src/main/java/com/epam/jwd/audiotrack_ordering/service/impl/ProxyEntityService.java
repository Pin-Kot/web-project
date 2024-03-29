package com.epam.jwd.audiotrack_ordering.service.impl;

import com.epam.jwd.audiotrack_ordering.db.TransactionManager;
import com.epam.jwd.audiotrack_ordering.entity.Entity;
import com.epam.jwd.audiotrack_ordering.service.EntityService;
import com.epam.jwd.audiotrack_ordering.service.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyEntityService<E extends Entity> implements InvocationHandler {

    private static final Logger LOG = LogManager.getLogger(ProxyEntityService.class);

    private final TransactionManager transactionManager;
    private final EntityService<E> entityService;
    private final Map<Method, Boolean> transactionalAnnotationsByMethods;

    private ProxyEntityService(TransactionManager transactionManager, EntityService<E> entityService) {
        this.transactionManager = transactionManager;
        this.entityService = entityService;
        this.transactionalAnnotationsByMethods = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityService<E>, E extends Entity> T of(T service) {
        return (T) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new ProxyEntityService<>(TransactionManager.getInstance(), service));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final boolean shouldWrapInTransaction = isTransactionalAnnotationPresentCaching(method);
        try {
            return invokeMethodInTransaction(method, args, shouldWrapInTransaction);
        } catch (InvocationTargetException e) {
            handleInvocationTargetException(method, shouldWrapInTransaction);
            throw e.getTargetException();
        }
    }

    private void handleInvocationTargetException(Method method, boolean transactionalAnnotationPresent) {
        LOG.error("exception occurred trying to invoke method: {}, Class: {}",
                method.getName(), method.getDeclaringClass().getSimpleName());
        if (transactionalAnnotationPresent) {
            transactionManager.commitTransaction();
            LOG.info("transaction committed because error occurred");
        }
    }

    private Object invokeMethodInTransaction(Method method, Object[] args, boolean shouldWrapInTransaction)
            throws IllegalAccessException, InvocationTargetException {
        if (shouldWrapInTransaction) {
            transactionManager.initTransaction();
            final Object result = method.invoke(entityService, args);
            transactionManager.commitTransaction();
            return result;
        } else {
            return method.invoke(entityService, args);
        }

    }

    private boolean isTransactionalAnnotationPresentCaching(Method method) {
        return transactionalAnnotationsByMethods
                .computeIfAbsent(method, this::isTransactionalAnnotationPresent);
    }

    private boolean isTransactionalAnnotationPresent(Method method) {
        try {
            return entityService.getClass()
                    .getMethod(method.getName(), method.getParameterTypes())
                    .isAnnotationPresent(Transactional.class);
        } catch (NoSuchMethodException e) {
            LOG.error("Method not found. Method {}, Class{}", method.getName(),
                    entityService.getClass().getSimpleName());
        }
        return false;
    }
}
