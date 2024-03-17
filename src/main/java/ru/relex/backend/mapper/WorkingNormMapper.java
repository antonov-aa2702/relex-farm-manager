package ru.relex.backend.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.entity.WorkingNorm;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.repository.ProductRepository;
import ru.relex.backend.repository.UserRepository;

/**
 * Реализация интерфейса {@link Mappable}  для преобразования {@link WorkingNorm}
 * в {@link WorkingNormDto} и обратно.
 * <p>
 * Переопределение некоторых методов необходимо, поскольку названия и типы полей
 * отличаются и MapStruct не может их замапить. Не реализованные методы генерируется
 * с помощью MapStruct.
 */
@Mapper(componentModel = "spring")
public abstract class WorkingNormMapper implements Mappable<WorkingNorm, WorkingNormDto> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public WorkingNorm toEntity(WorkingNormDto dto) {
        return WorkingNorm.builder()
                .id(dto.getId())
                .product(productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new ResourceIllegalStateException("Product not found")))
                .user(userRepository.findById(dto.getEmployeeId())
                        .orElseThrow(() -> new ResourceIllegalStateException("User not found")))
                .deadline(dto.getDeadline())
                .workingCount(dto.getWorkingCount())
                .currentCount(dto.getCurrentCount())
                .status(dto.getStatus())
                .build();
    }

    @Override
    public WorkingNormDto toDto(WorkingNorm entity) {
        return WorkingNormDto.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .employeeId(entity.getUser().getId())
                .workingCount(entity.getWorkingCount())
                .currentCount(entity.getCurrentCount())
                .deadline(entity.getDeadline())
                .status(entity.getStatus())
                .build();
    }
}
