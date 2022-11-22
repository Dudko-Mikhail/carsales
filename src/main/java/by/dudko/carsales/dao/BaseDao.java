package by.dudko.carsales.dao;

import by.dudko.carsales.exception.DaoException;
import by.dudko.carsales.model.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T, E extends BaseEntity<T>> {
    List<E> findAll() throws DaoException;
    Optional<E> findById(T id) throws DaoException;
    void insert(E entity) throws DaoException;
    boolean deleteById(T id) throws DaoException;

}
