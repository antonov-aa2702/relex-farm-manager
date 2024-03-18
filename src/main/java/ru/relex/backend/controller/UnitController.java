package ru.relex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.dto.validation.Creatable;
import ru.relex.backend.service.UnitService;

import java.util.List;

/**
 * Контроллер, обрабатывающий HTTP-запросы, связанные с единицами измерениями.
 */
@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
@Tag(name = "Units", description = "Units API")
public class UnitController {

    private final UnitService unitService;

    /**
     * Производит добавление новой единицы измерения в систему. Запрос может осуществлять
     * только владелец фермы. Запрещается повторно добавлять уже зарегистрированные
     * единицы измерения.
     *
     * @param unitDto данные о единице измерения, которые будут добавлены
     * @return данные об добавленной единице измерения
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление единицы измерения")
    public UnitDto addUnit(@Validated(Creatable.class) @RequestBody UnitDto unitDto) {
        return unitService.create(unitDto);
    }

    /**
     * Метод для получения всех единиц измерения. Используется в качестве справочной информации
     * для владельца. Может быть использован при добавлении новых единиц измерения.
     *
     * @return список всех зарегистрированных единиц измерения.
     */
    @GetMapping
    @Operation(summary = "Получение всех единиц измерения")
    public List<UnitDto> getAllUnits() {
        return unitService.getAll();
    }
}