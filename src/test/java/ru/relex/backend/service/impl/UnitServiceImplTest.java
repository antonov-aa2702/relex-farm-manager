package ru.relex.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.relex.backend.annotation.UnitTest;
import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.entity.Unit;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.UnitMapper;
import ru.relex.backend.repository.UnitRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class UnitServiceImplTest {

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private UnitMapper unitMapper;

    @InjectMocks
    private UnitServiceImpl unitService;


    @Test
    void testGetByNameIfUnitExists() {
        String unitName = "unit";
        int unitId = 1;
        Unit unit = getDefaultUnit(unitId, unitName);
        when(unitRepository.findByNameIgnoreCase(unitName))
                .thenReturn(Optional.of(unit));
        UnitDto unitDto = getDefaultUnitDto(unitId, unitName);
        when(unitMapper.toDto(unit))
                .thenReturn(unitDto);

        UnitDto actualResult = unitService.getByName(unitName);

        assertThat(actualResult)
                .isEqualTo(unitDto);
        verify(unitRepository).findByNameIgnoreCase(unitName);
        verify(unitMapper).toDto(unit);
    }

    @Test
    void testGetByNameIfUnitDoesNotExists() {
        String unitName = "unit";
        when(unitRepository.findByNameIgnoreCase(unitName))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> unitService
                        .getByName(unitName))
                .withMessage("Единица измерения не найдена");
        verify(unitRepository).findByNameIgnoreCase(unitName);
        verifyNoInteractions(unitMapper);
    }

    @Test
    void createIfUnitDoesNotExists() {
        String unitName = "unit";
        Unit unit = getDefaultUnit(1, unitName);
        UnitDto unitDto = getDefaultUnitDto(1, unitName);
        when(unitRepository.findByNameIgnoreCase(unitName))
                .thenReturn(Optional.empty());
        when(unitRepository.save(unit))
                .thenReturn(unit);
        when(unitMapper.toEntity(unitDto))
                .thenReturn(unit);
        when(unitMapper.toDto(unit))
                .thenReturn(unitDto);

        UnitDto actualResult = unitService
                .create(unitDto);

        assertThat(actualResult)
                .isEqualTo(unitDto);
        verify(unitRepository).findByNameIgnoreCase(unitName);
        verify(unitRepository).save(unit);
        verify(unitMapper).toDto(unit);
    }

    @Test
    void createIfUnitExists() {
        String unitName = "unit";
        Unit unit = getDefaultUnit(1, unitName);
        UnitDto unitDto = getDefaultUnitDto(1, unitName);
        when(unitRepository.findByNameIgnoreCase(unitName))
                .thenReturn(Optional.of(unit));

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> unitService
                        .create(unitDto))
                .withMessage("Единица измерения уже существует");
        verify(unitRepository).findByNameIgnoreCase(unitName);
        verifyNoInteractions(unitMapper);
    }


    @Test
    void getAll() {
        Unit unit = getDefaultUnit(1, "unit");
        when(unitRepository.findAll())
                .thenReturn(List.of(unit));
        UnitDto unitDto = getDefaultUnitDto(1, "unit");
        when(unitMapper.toDto(unit))
                .thenReturn(unitDto);

        List<UnitDto> actualResult = unitService
                .getAll();

        assertThat(actualResult)
                .hasSize(1);
        assertThat(actualResult.get(0))
                .isEqualTo(unitDto);
        verify(unitMapper).toDto(unit);
        verify(unitRepository).findAll();
    }


    private Unit getDefaultUnit(int id, String name) {
        return Unit.builder()
                .id(id)
                .name(name)
                .build();
    }

    private UnitDto getDefaultUnitDto(int id, String name) {
        return UnitDto.builder()
                .id(id)
                .name(name)
                .build();
    }
}
