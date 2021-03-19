package ru.topjava.lunchvote.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaUtil {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> void evictCache(Class<T> region) {
        Session session = (Session) entityManager.getDelegate();
        SessionFactory sessionFactory = session.getSessionFactory();
        sessionFactory.getCache().evict(region);
    }
}
