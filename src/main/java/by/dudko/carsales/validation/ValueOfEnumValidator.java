package by.dudko.carsales.validation;

import by.dudko.carsales.validation.annotation.ValueOfEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> enumValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        String[] except = annotation.except();
        enumValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
        enumValues.removeAll(Arrays.asList(except));
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("must be one of " + enumValues)
                .addConstraintViolation();
        return enumValues.contains(value.toString());
    }
}
