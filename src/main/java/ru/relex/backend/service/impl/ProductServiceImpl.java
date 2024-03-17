package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.product.UnitDto;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.Unit;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.ProductMapper;
import ru.relex.backend.mapper.UnitMapper;
import ru.relex.backend.repository.ProductRepository;
import ru.relex.backend.service.ProductService;
import ru.relex.backend.service.UnitService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация {@link ProductService}
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UnitService unitService;
    private final ProductMapper productMapper;
    private final UnitMapper unitMapper;

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public ProductDto getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceIllegalStateException("Продукт не найден"));
    }

    @Override
    @Transactional
    @Loggable
    public ProductDto create(ProductDto productDto) {
        Unit unit = getUnitFromProductDto(productDto);
        Optional<Product> mayBeProduct = productRepository.findByNameIgnoreCaseAndUnit(
                productDto.getName(),
                unit);
        if (mayBeProduct.isPresent()) {
            throw new ResourceIllegalStateException("Продукт уже существует");
        }
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    private Unit getUnitFromProductDto(ProductDto productDto) {
        UnitDto unitDto = unitService.getByName(productDto.getUnit());
        return unitMapper.toEntity(unitDto);
    }
}
