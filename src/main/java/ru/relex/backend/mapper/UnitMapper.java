package ru.relex.backend.mapper;

import org.mapstruct.Mapper;
import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.entity.Unit;

/**
 * Интерфейс для преобразования {@link Unit} в {@link UnitDto} и обратно.
 * Реализация полностью генерируется с помощью MapStruct.
 */
@Mapper(componentModel = "spring")
public interface UnitMapper extends Mappable<Unit, UnitDto> {
}
