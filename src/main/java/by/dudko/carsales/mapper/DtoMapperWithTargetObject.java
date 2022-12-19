package by.dudko.carsales.mapper;

public interface DtoMapperWithTargetObject<S, T> {
    T map(S source, T target);
}
