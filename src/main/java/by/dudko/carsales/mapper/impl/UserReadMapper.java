package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements DtoMapper<User, UserReadDto> {
    @Override
    public UserReadDto map(User source) {
        return UserReadDto.builder()
                .id(source.getId())
                .email(source.getEmail())
                .phoneNumber(source.getPhoneNumber())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
