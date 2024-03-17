package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.filter.WorkingNormFilter;
import ru.relex.backend.dto.product.ManufacturedProductDto;
import ru.relex.backend.dto.product.ProductDto;
import ru.relex.backend.dto.product.WorkingNormDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.Role;
import ru.relex.backend.entity.Status;
import ru.relex.backend.entity.User;
import ru.relex.backend.entity.WorkingNorm;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.ProductMapper;
import ru.relex.backend.mapper.UserMapper;
import ru.relex.backend.mapper.WorkingNormMapper;
import ru.relex.backend.repository.WorkingNormRepository;
import ru.relex.backend.service.ProductService;
import ru.relex.backend.service.UserService;
import ru.relex.backend.service.WorkingNormService;
import ru.relex.backend.util.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация {@link WorkingNormService}
 */
@Service
@RequiredArgsConstructor
public class WorkingNormServiceImpl implements WorkingNormService {

    private final WorkingNormRepository workingNormRepository;
    private final ProductService productService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final WorkingNormMapper workingNormMapper;


    @Override
    @Transactional
    @Loggable
    public WorkingNormDto create(WorkingNormDto workingNormDto) {
        checkDeadlineValid(workingNormDto);
        Optional<WorkingNorm> mayBeWorkingNorm = getInProgressWorkingNormByProductIdAndEmployeeId(
                workingNormDto.getProductId(),
                workingNormDto.getEmployeeId());
        if (mayBeWorkingNorm.isPresent()) {
            throw new ResourceIllegalStateException("Рабочая норма уже существует");
        }
        Product product = getProductById(workingNormDto.getProductId());
        User user = getUserByEmployeeId(workingNormDto.getEmployeeId());
        checkValidUser(user);
        WorkingNorm workingNorm = createWorkingNorm(workingNormDto, product, user);
        workingNormRepository.save(workingNorm);
        return workingNormMapper.toDto(workingNorm);
    }

    private static void checkDeadlineValid(WorkingNormDto workingNormDto) {
        if (workingNormDto.getDeadline().isBefore(LocalDateTime.now())) {
            throw new ResourceIllegalStateException("Нельзя создать рабочую норму задним числом");
        }
        LocalDateTime offsetTime = workingNormDto.getDeadline().plusHours(1);
        if (!LocalDateTimeUtil.isTodayDeadlineBefore(offsetTime, 18, 0)) {
            throw new ResourceIllegalStateException("Рабочий день заканчивается в 18:00. Последнюю задачу можно выдать в 17:00.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public WorkingNormDto getAccessWorkingNorm(ManufacturedProductDto manufacturedProductDto, Long employeeId) {
        Optional<WorkingNorm> mayBeWorkingNorm = getInProgressWorkingNormByProductIdAndEmployeeId(
                manufacturedProductDto.getProductId(),
                employeeId);
        if (mayBeWorkingNorm.isEmpty()) {
            throw new ResourceIllegalStateException("Рабочая нормы нет");
        }
        return workingNormMapper.toDto(mayBeWorkingNorm.get());
    }

    @Override
    @Transactional
    @Loggable
    public WorkingNormDto updateWorkingNorm(WorkingNormDto workingNormDto, Integer count) {
        Optional<WorkingNorm> mayBeWorkingNorm = workingNormRepository.findById(workingNormDto.getId());
        if (mayBeWorkingNorm.isEmpty()) {
            throw new ResourceIllegalStateException("Рабочая норма не существует");
        }
        WorkingNorm workingNorm = mayBeWorkingNorm.get();
        int updatedCurrentNorm = workingNorm.getCurrentCount() + count;
        workingNorm.setCurrentCount(updatedCurrentNorm);
        workingNormRepository.save(workingNorm);
        return workingNormMapper.toDto(workingNorm);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<WorkingNormDto> getWorkingNormsByFilter(Long employeeId, WorkingNormFilter workingNormFilter) {
        User user = getUserByEmployeeId(employeeId);
        if (user.getRole().equals(Role.ROLE_OWNER)) {
            throw new ResourceIllegalStateException("Пользователь не может получить рабочую норму");
        }
        List<WorkingNorm> workingNormsByFilter =
                workingNormRepository.findByUserAndStatusAndDeadlineBetweenOrderByDeadline(
                        user,
                        workingNormFilter.getStatus(),
                        workingNormFilter.startTime,
                        workingNormFilter.endTime
                );
        return workingNormMapper.toDto(workingNormsByFilter);
    }

    @Override
    @Transactional
    @Loggable
    public void updateWorkingNorm(WorkingNormDto workingNormDto) {
        WorkingNorm workingNorm = workingNormMapper.toEntity(workingNormDto);
        workingNormRepository.save(workingNorm);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<WorkingNormDto> getWorkingNormByStatus(Status status) {
        List<WorkingNorm> workingNormsByStatus = workingNormRepository.findByStatus(status);
        return workingNormMapper.toDto(workingNormsByStatus);
    }

    private Optional<WorkingNorm> getInProgressWorkingNormByProductIdAndEmployeeId(Long productId, Long employeeId) {
        ProductDto productDto = productService.getById(productId);
        Product product = productMapper.toEntity(productDto);
        UserDto userDto = userService.getById(employeeId);
        User user = userMapper.toEntity(userDto);
        return workingNormRepository
                .findByProductAndUserAndStatus(product, user, Status.IN_PROGRESS);
    }


    private void checkValidUser(User user) {
        if (user.getRole().equals(Role.ROLE_OWNER)) {
            throw new ResourceIllegalStateException("Пользователь не может получить рабочую норму");
        }
        if (user.getEnabled().equals(false)) {
            throw new ResourceIllegalStateException("Аккаунт пользователя заблокирован");
        }
    }

    private WorkingNorm createWorkingNorm(WorkingNormDto workingNormDto, Product product, User user) {
        return WorkingNorm.builder()
                .product(product)
                .user(user)
                .workingCount(workingNormDto.getWorkingCount())
                .currentCount(0)
                .deadline(workingNormDto.getDeadline())
                .status(Status.IN_PROGRESS)
                .build();
    }

    private User getUserByEmployeeId(Long employeeId) {
        UserDto userDto = userService.getById(employeeId);
        return userMapper.toEntity(userDto);
    }

    private Product getProductById(Long productId) {
        ProductDto productDto = productService
                .getById(productId);
        return productMapper.toEntity(productDto);
    }
}
