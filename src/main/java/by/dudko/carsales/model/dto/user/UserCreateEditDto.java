package by.dudko.carsales.model.dto.user;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class UserCreateEditDto {
    @Email
    @NotEmpty
    @Length(max = 64)
    String email;

    @NotEmpty
    @Length(max = 32)
    String phoneNumber;
}
