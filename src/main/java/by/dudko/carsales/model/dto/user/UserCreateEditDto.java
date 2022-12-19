package by.dudko.carsales.model.dto.user;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class UserCreateEditDto {
    @Email
    @NotEmpty
    String email;

    @NotEmpty
    String phoneNumber;
}
