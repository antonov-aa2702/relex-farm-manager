package ru.relex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.relex.backend.dto.filter.WorkingNormFilter;
import ru.relex.backend.dto.product.ManufacturedProductDto;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.dto.validation.Creatable;
import ru.relex.backend.service.ManufacturedProductService;
import ru.relex.backend.service.WorkingNormService;

import java.util.List;

/**
 * Контроллер, который обрабатывает HTTP-запросы, связанные с работой сотрудников фермы.
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employees API")
public class EmployeeController {

    private final ManufacturedProductService manufacturedProductService;
    private final WorkingNormService workingNormService;

    /**
     * Осуществляет добавление произведенного продукта в систему конкретным сотрудником.
     * Запрещено добавлять продукты для чужих сотрудников.
     *
     * @param id         идентификатор сотрудника, который добавляет продукт
     * @param productDto продукт, который будет добавлен
     * @return данные, содержащие информацию об оставшемся количестве продуктов,
     * необходимых для достижения рабочей нормы
     */
    @PostMapping("/{id}/products")
    @PreAuthorize("@customSecurityExpression.canAccessEmployee(#id)")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Производство продукта")
    public WorkingNormDto produceProduct(@PathVariable("id") Long id,
                                         @Validated(Creatable.class)
                                         @RequestBody ManufacturedProductDto productDto) {
        return manufacturedProductService.produceProduct(productDto, id);
    }

    /**
     * Получение всех рабочих норм конкретного сотрудника.
     * Запрещено получать рабочие нормы чужих сотрудников.
     *
     * @param id                идентификатор сотрудника
     * @param workingNormFilter фильтр для получения рабочих норм
     * @return найденные рабочие нормы
     */
    @GetMapping("/{id}/working-norms")
    @PreAuthorize("@customSecurityExpression.canAccessEmployee(#id)")
    @Operation(summary = "Получение рабочих норм")
    public List<WorkingNormDto> getWorkingNorms(@PathVariable("id") Long id,
                                                @Validated(Creatable.class) WorkingNormFilter workingNormFilter) {
        return workingNormService.getWorkingNormsByFilter(id, workingNormFilter);
    }
}
