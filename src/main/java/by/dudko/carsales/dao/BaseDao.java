package by.dudko.carsales.dao;

import by.dudko.carsales.model.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T, E extends BaseEntity<T>> {
    List<E> findAll();
    Optional<E> findById(T id);
    void insert(E entity);
    boolean deleteById(T id);
}
