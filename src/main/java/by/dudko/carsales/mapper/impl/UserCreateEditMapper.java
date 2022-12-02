package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.mapper.DtoMapperWithTargetObject;
import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import by.dudko.carsales.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreateEditMapper implements DtoMapper<UserCreateEditDto, User>,
        DtoMapperWithTargetObject<UserCreateEditDto, User> {
    @Override
    public User map(UserCreateEditDto source) {
        return copy(source, new User());
    }

    @Override
    public User map(UserCreateEditDto source, User target) {
        return copy(source, target);
    }

    private User copy(UserCreateEditDto source, User target) {
        target.setEmail(source.getEmail());
        target.setPhoneNumber(source.getPhoneNumber());
        return target;
    }
}
