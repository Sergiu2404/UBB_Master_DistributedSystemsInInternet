package com.example.dropwizard.dao;

import com.example.dropwizard.entities.Car;
import io.dropwizard.hibernate.AbstractDAO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.SessionFactory;

import java.util.List;

public class CarDAO extends AbstractDAO<Car> {
    public CarDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Car> findAll() {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        Root<Car> root = query.from(Car.class);
        query.select(root);
        return currentSession().createQuery(query).getResultList();
    }

    public Car findById(Long id) {
        return get(id);
    }

    public Car create(Car user) {
        return persist(user);
    }
}