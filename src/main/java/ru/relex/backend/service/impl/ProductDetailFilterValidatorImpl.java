package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.relex.backend.dto.filter.ProductDetailFilter;
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.Role;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.service.ProductDetailFilterValidator;
import ru.relex.backend.service.ProductService;
import ru.relex.backend.service.UserService;

/**
 * Реализация {@link ProductDetailFilterValidator}
 */
@Service
@RequiredArgsConstructor
public class ProductDetailFilterValidatorImpl implements ProductDetailFilterValidator {

    private final UserService userService;
    private final ProductService productService;

    @Override
    public void validateProductDetailFilter(ProductDetailFilter productDetailFilter) {
        Long employeeId = productDetailFilter.getEmployeeId();
        Long productId = productDetailFilter.getProductId();
        if (employeeId != null) {
            UserDto userDto = userService.getById(employeeId);
            if (userDto.getRole().equals(Role.ROLE_OWNER)) {
                throw new ResourceIllegalStateException("Пользователь не является сотрудником фермы");
            }
        }
        if (productId != null) {
            ProductDto product = productService.getById(productDetailFilter.getProductId());
        }
    }
}
