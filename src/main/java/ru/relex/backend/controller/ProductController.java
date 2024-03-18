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
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.product.ReportDetailsDto;
import ru.relex.backend.service.ProductService;
import ru.relex.backend.service.ReportService;

import java.util.List;

/**
 * Контроллер, обрабатывающий HTTP-запросы, связанные с продуктами.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Products API")
public class ProductController {

    private final ProductService productService;
    private final ReportService reportService;

    /**
     * Добавление нового продукта в систему. Добавление может производить только владелец фермы.
     * Не допускается повторно добавлять уже зарегистрированные продукты.
     *
     * @param productDto дынные о продукте, который будет добавлен
     * @return данные о добавленном продукте
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление продукта")
    public ProductDto addProduct(@Validated @RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }

    /**
     * Осуществляет выдачу отчета по произведенным продуктам на ферме.
     * Доступ к получению отчета имеет только владелец фермы.
     *
     * @param productDetailFilter фильтр для получения отчета
     * @return отчетные данные
     */
    @GetMapping("/report")
    @Operation(summary = "Получение отчёта")
    public List<ReportDetailsDto> getReport(@Validated ProductDetailFilter productDetailFilter) {
        return reportService.getReportByProductDetailFilter(productDetailFilter);
    }

    /**
     * Метод для получения всех продуктов. Используется в качестве справочной информации для владельца.
     * Может быть использован при добавлении новых продуктов.
     *
     * @return список всех зарегистрированных продуктов в системе
     */
    @GetMapping()
    @Operation(summary = "Получение всех продуктов")
    public List<ProductDto> getAllProducts() {
        return productService.getAll();
    }
}