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
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.dto.product.AnalyticDetailsDto;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.dto.validation.Creatable;
import ru.relex.backend.service.AnalyticsService;
import ru.relex.backend.service.WorkingNormService;

import java.util.List;

/**
 * Контроллер обрабатывающий HTTP-запросы, связанные с рабочими нормами.
 */
@RestController
@RequestMapping("/api/v1/working-norms")
@RequiredArgsConstructor
@Tag(name = "Working Norms", description = "Working Norms API")
public class WorkingNormController {

    private final AnalyticsService analyticsService;
    private final WorkingNormService workingNormService;

    /**
     * Осуществляет добавление рабочей нормы в систему. Добавление может производить только
     * владелец фермы. Запрещено добавлять рабочую норму задним числом. Разрешается добавлять рабочую норму
     * только на сегодняшний день. Время установления рабочей нормы устанавливается в соответствии
     * с требованиями владельца фермы. Не допускается повторно добавлять рабочую норму для одного и того же продукта
     * и одного и того же сотрудника при действующей рабочей нормы для данного продукта и сотрудника. После выполнения
     * такой рабочей нормы это разрешается делать.
     *
     * @param workingNormDto данные рабочей нормы
     * @return данные об добавленной рабочей норме
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление рабочей нормы")
    public WorkingNormDto addWorkingNorm(@Validated(Creatable.class)
                                         @RequestBody WorkingNormDto workingNormDto) {
        return workingNormService.create(workingNormDto);
    }

    /**
     * Осуществляет получение аналитического отчета по фильтру.
     * Доступ к получению отчета имеет только владелец фермы.
     *
     * @param productDetailFilter фильтр для получения аналитического отчета
     * @return аналитический отчет
     */
    @GetMapping("/analytics")
    @Operation(summary = "Получение аналитики")
    public List<AnalyticDetailsDto> getAnalytics(@Validated(Creatable.class)
                                                 ProductDetailFilter productDetailFilter) {
        return analyticsService.getAnalyticsByProductDetailFilter(productDetailFilter);
    }
}