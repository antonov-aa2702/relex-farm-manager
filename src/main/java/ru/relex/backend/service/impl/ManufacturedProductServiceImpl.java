package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.product.ManufacturedProductDto;
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.ManufacturedProduct;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.User;
import ru.relex.backend.mapper.ProductMapper;
import ru.relex.backend.mapper.UserMapper;
import ru.relex.backend.repository.ManufacturedProductRepository;
import ru.relex.backend.service.ManufacturedProductService;
import ru.relex.backend.service.ProductService;
import ru.relex.backend.service.UserService;
import ru.relex.backend.service.WorkingNormService;

import java.time.LocalDateTime;

/**
 * Реализация {@link ManufacturedProductService}
 */
@Service
@RequiredArgsConstructor
public class ManufacturedProductServiceImpl implements ManufacturedProductService {

    private final WorkingNormService workingNormService;
    private final ManufacturedProductRepository manufacturedProductRepository;
    private final ProductService productService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    @Loggable
    public WorkingNormDto produceProduct(ManufacturedProductDto manufacturedProductDto, Long employeeId) {
        WorkingNormDto accessWorkingNorm = workingNormService
                .getAccessWorkingNorm(manufacturedProductDto, employeeId);
        WorkingNormDto updatedWorkingNorm = workingNormService
                .updateWorkingNorm(accessWorkingNorm, manufacturedProductDto.getCount());
        manufacturedProductRepository.save(getManufacturedProduct(manufacturedProductDto, employeeId));
        return updatedWorkingNorm;
    }

    private ManufacturedProduct getManufacturedProduct(ManufacturedProductDto manufacturedProductDto, Long employeeId) {
        Product product = getProductById(manufacturedProductDto.getProductId());
        User user = getUserById(employeeId);
        return ManufacturedProduct.builder()
                .product(product)
                .user(user)
                .count(manufacturedProductDto.getCount())
                .time(LocalDateTime.now())
                .build();

    }

    private User getUserById(Long employeeId) {
        UserDto userDto = userService.getById(employeeId);
        return userMapper.toEntity(userDto);
    }

    private Product getProductById(Long productId) {
        ProductDto productDto = productService.getById(productId);
        return productMapper.toEntity(productDto);
    }
}
