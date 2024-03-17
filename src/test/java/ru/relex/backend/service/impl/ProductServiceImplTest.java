package ru.relex.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.relex.backend.annotation.UnitTest;
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.Unit;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.ProductMapper;
import ru.relex.backend.mapper.UnitMapper;
import ru.relex.backend.repository.ProductRepository;
import ru.relex.backend.service.UnitService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UnitService unitService;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private UnitMapper unitMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testGetByIdIfProductExists() {
        long productId = 1L;
        String productName = "product";
        int unitId = 1;
        String unitName = "unit";
        Unit unit = getDefaultUnit(unitId, unitName);
        Product product = getDrfaultProduct(productId, productName, unit);
        ProductDto productDto = getDefaultProductDto(productId, unitName, productName);
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        when(productMapper.toDto(product))
                .thenReturn(productDto);

        ProductDto actualResult = productService
                .getById(productId);

        assertThat(actualResult)
                .isEqualTo(productDto);
        verify(productRepository).findById(productId);
        verify(productMapper).toDto(product);
    }

    @Test
    void testGetByIdIfProductDoesNotExists() {
        long productId = 1L;
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> productService
                        .getById(productId))
                .withMessage("Продукт не найден");
        verify(productRepository).findById(productId);
        verifyNoInteractions(productMapper);
    }


    @Test
    void createProductIfProductDoesNotExists() {
        long productId = 1L;
        String productName = "product";
        int unitId = 1;
        String unitName = "unit";
        Unit unit = getDefaultUnit(unitId, unitName);
        Product product = getDrfaultProduct(productId, productName, unit);
        ProductDto productDto = getDefaultProductDto(productId, unitName, productName);
        UnitDto unitDto = getDefaultUnitDto(unitId, unitName);

        when(unitService.getByName(unitName))
                .thenReturn(unitDto);
        when(unitMapper.toEntity(unitDto))
                .thenReturn(unit);
        when(productRepository.findByNameIgnoreCaseAndUnit(productName, unit))
                .thenReturn(Optional.empty());
        when(productMapper.toEntity(productDto))
                .thenReturn(product);
        when(productRepository.save(product))
                .thenReturn(product);
        when(productMapper.toDto(product))
                .thenReturn(productDto);

        ProductDto actualResult = productService.create(productDto);

        assertThat(actualResult)
                .isEqualTo(productDto);
        verify(unitService).getByName(unitName);
        verify(unitMapper).toEntity(unitDto);
        verify(productRepository).findByNameIgnoreCaseAndUnit(productName, unit);
        verify(productMapper).toEntity(productDto);
        verify(productRepository).save(product);
        verify(productMapper).toDto(product);
    }

    @Test
    void createProductIfProductExists() {
        long productId = 1L;
        String productName = "product";
        int unitId = 1;
        String unitName = "unit";
        Unit unit = getDefaultUnit(unitId, unitName);
        Product product = getDrfaultProduct(productId, productName, unit);
        ProductDto productDto = getDefaultProductDto(productId, unitName, productName);
        UnitDto unitDto = getDefaultUnitDto(unitId, unitName);
        when(unitService.getByName(unitName))
                .thenReturn(unitDto);
        when(unitMapper.toEntity(unitDto))
                .thenReturn(unit);
        when(productRepository.findByNameIgnoreCaseAndUnit(productName, unit))
                .thenReturn(Optional.of(product));

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> productService
                        .create(productDto))
                .withMessage("Продукт уже существует");
        verify(unitService).getByName(unitName);
        verify(unitMapper).toEntity(unitDto);
        verify(productRepository).findByNameIgnoreCaseAndUnit(productName, unit);
        verifyNoInteractions(productMapper);
        verify(productRepository, never()).save(product);
    }

    private UnitDto getDefaultUnitDto(int unitId, String unitName) {
        return UnitDto.builder()
                .id(unitId)
                .name(unitName)
                .build();
    }

    private ProductDto getDefaultProductDto(long productId, String unitName, String productName) {
        return ProductDto.builder()
                .id(productId)
                .name(productName)
                .unit(unitName)
                .build();
    }

    private Product getDrfaultProduct(long productId, String productName, Unit unit) {
        return Product.builder()
                .id(productId)
                .name(productName)
                .unit(unit)
                .build();
    }

    private Unit getDefaultUnit(int unitId, String unitName) {
        return Unit.builder()
                .id(unitId)
                .name(unitName)
                .build();
    }
}
