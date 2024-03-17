package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.entity.Unit;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.UnitMapper;
import ru.relex.backend.repository.UnitRepository;
import ru.relex.backend.service.UnitService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация {@link UnitService}
 */
@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public UnitDto getByName(String unit) {
        return unitRepository.findByNameIgnoreCase(unit)
                .map(unitMapper::toDto)
                .orElseThrow(() -> new ResourceIllegalStateException("Единица измерения не найдена"));
    }

    @Override
    @Transactional
    @Loggable
    public UnitDto create(UnitDto unitDto) {
        Optional<Unit> mayBeUnitDto = unitRepository.findByNameIgnoreCase(unitDto.getName());
        if (mayBeUnitDto.isPresent()) {
            throw new ResourceIllegalStateException("Единица измерения уже существует");
        }
        Unit unit = unitMapper.toEntity(unitDto);
        unitRepository.save(unit);
        return unitMapper.toDto(unit);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<UnitDto> getAll() {
        return unitRepository.findAll().stream()
                .map(unitMapper::toDto)
                .toList();
    }
}
