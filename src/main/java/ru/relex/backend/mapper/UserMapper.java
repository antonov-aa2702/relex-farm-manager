package ru.relex.backend.mapper;

import org.mapstruct.Mapper;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.User;

/**
 * Интерфейс для преобразования {@link User} в {@link UserDto} и обратно.
 * Реализация полностью генерируется с помощью MapStruct.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
