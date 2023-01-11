package by.dudko.carsales.model.dto.user;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class UserCreateEditDto {
    @Email
    @NotEmpty
    @Length(max = 64)
    String email;

    @NotNull
    @Length(max = 32, min = 3)
    String phoneNumber;
}
