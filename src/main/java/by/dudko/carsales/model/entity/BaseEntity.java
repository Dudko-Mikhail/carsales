package by.dudko.carsales.model.entity;

public interface BaseEntity<T> {
    T getId();

    void setId(T id);
}
