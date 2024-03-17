package ru.relex.backend.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.Unit;
import ru.relex.backend.repository.UnitRepository;
import ru.relex.backend.util.StringUtil;

/**
 * Реализация интерфейса {@link Mappable}  для преобразования {@link Product} в {@link ProductDto} и обратно.
 * <p>
 *  Переопределение некоторых методов необходимо, поскольку названия и типы полей отличаются и MapStruct не может их замапить.
 *  Явно нереализованные методы генерируется с помощью MapStruct.
 */
@Mapper(componentModel = "spring")
public abstract class ProductMapper implements Mappable<Product, ProductDto> {

    @Autowired
    private UnitRepository unitRepository;

    @Override
    public ProductDto toDto(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .unit(entity.getUnit().getName())
                .build();
    }

    @Override
    public Product toEntity(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .name(StringUtil.getCapitalizeText(dto.getName()))
                .unit(unitRepository.findByNameIgnoreCase(dto.getUnit()).orElse(null))
                .build();
    }
}
