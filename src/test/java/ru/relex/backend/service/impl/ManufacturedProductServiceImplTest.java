package ru.relex.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.relex.backend.annotation.UnitTest;
import ru.relex.backend.dto.product.ManufacturedProductDto;
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.ManufacturedProduct;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.Role;
import ru.relex.backend.entity.Unit;
import ru.relex.backend.entity.User;
import ru.relex.backend.mapper.ProductMapper;
import ru.relex.backend.mapper.UserMapper;
import ru.relex.backend.repository.ManufacturedProductRepository;
import ru.relex.backend.service.ProductService;
import ru.relex.backend.service.UserService;
import ru.relex.backend.service.WorkingNormService;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class ManufacturedProductServiceImplTest {
    @Mock
    private WorkingNormService workingNormService;
    @Mock
    private ManufacturedProductRepository manufacturedProductRepository;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ManufacturedProductServiceImpl manufacturedProductService;

    @Test
    void testProduceProduct() {
        long defaultId = 1L;
        int unitId = 1;
        String productName = "product";
        String unitName = "unit";
        ManufacturedProductDto manufacturedProductDto = getdDefaultManufacturedProductDto(defaultId);
        WorkingNormDto workingNormDto = getDefaultWorkingDto(defaultId, defaultId, defaultId);
        UserDto userDto = getDefaultUserDto();
        User user = getDefaultUser();
        Unit unit = getDefaultUnit(unitId, unitName);
        Product product = getDefaultProduct(defaultId, productName, unit);
        ProductDto productDto = getDefaultProductDto(defaultId, unitName, productName);
        when(workingNormService.getAccessWorkingNorm(manufacturedProductDto, defaultId))
                .thenReturn(workingNormDto);
        workingNormDto.setCurrentCount(manufacturedProductDto.getCount() + workingNormDto.getCurrentCount());
        when(workingNormService.updateWorkingNorm(workingNormDto, manufacturedProductDto.getCount()))
                .thenReturn(workingNormDto);
        when(productService.getById(defaultId))
                .thenReturn(productDto);
        when(productMapper.toEntity(productDto))
                .thenReturn(product);
        when(userService.getById(defaultId))
                .thenReturn(userDto);
        when(userMapper.toEntity(userDto))
                .thenReturn(user);
        when(userService.getById(defaultId))
                .thenReturn(userDto);
        when(userMapper.toEntity(userDto))
                .thenReturn(user);

        WorkingNormDto actualResult = manufacturedProductService
                .produceProduct(manufacturedProductDto, defaultId);

        assertThat(actualResult)
                .isEqualTo(workingNormDto);
        verify(workingNormService)
                .getAccessWorkingNorm(manufacturedProductDto, defaultId);
        verify(workingNormService)
                .updateWorkingNorm(workingNormDto, manufacturedProductDto.getCount());
        verify(manufacturedProductRepository)
                .save(Mockito.any(ManufacturedProduct.class));
        verify(productService)
                .getById(defaultId);
        verify(productMapper)
                .toEntity(productDto);
        verify(userService)
                .getById(defaultId);
        verify(userMapper)
                .toEntity(userDto);
        verify(userService)
                .getById(defaultId);
        verify(userMapper)
                .toEntity(userDto);
    }


    private Unit getDefaultUnit(int unitId, String unitName) {
        return Unit.builder()
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

    private Product getDefaultProduct(long productId, String productName, Unit unit) {
        return Product.builder()
                .id(productId)
                .name(productName)
                .unit(unit)
                .build();
    }

    private User getDefaultUser() {
        return User.builder()
                .id(1L)
                .email("email")
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .password("12345")
                .role(Role.ROLE_EMPLOYEE)
                .enabled(true)
                .build();
    }

    private UserDto getDefaultUserDto() {
        return UserDto.builder()
                .id(1L)
                .email("email")
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .password("123456")
                .role(Role.ROLE_EMPLOYEE)
                .enabled(true)
                .build();
    }

    private WorkingNormDto getDefaultWorkingDto(long id, long productId, long employeeId) {
        return WorkingNormDto.builder()
                .id(id)
                .productId(productId)
                .employeeId(employeeId)
                .currentCount(0)
                .workingCount(10)
                .build();
    }

    private ManufacturedProductDto getdDefaultManufacturedProductDto(long defaultId) {
        return ManufacturedProductDto.builder()
                .productId(defaultId)
                .count(5)
                .time(LocalDateTime.now())
                .build();
    }
}
