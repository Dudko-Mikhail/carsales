package by.dudko.carsales.validation;

import by.dudko.carsales.validation.annotation.ExistsInDb;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class ExistsInDbValidator implements ConstraintValidator<ExistsInDb, Object> {
    private final SessionFactory sessionFactory;
    private Class<?> entityClass;

    @Override
    public void initialize(ExistsInDb constraintAnnotation) {
        entityClass = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(Object id, ConstraintValidatorContext context) {
        try (var session = sessionFactory.openSession()) {
            var searchResult = Optional.ofNullable(session.find(entityClass, id));
            return searchResult.isPresent();
        }
    }
}
