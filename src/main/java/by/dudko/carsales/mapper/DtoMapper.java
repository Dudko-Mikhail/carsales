package by.dudko.carsales.mapper;

public interface DtoMapper<S, T> {
    T map(S source);
}
