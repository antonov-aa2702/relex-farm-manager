package ru.relex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.service.UserService;

/**
 * Контроллер, обрабатывающий HTTP-запросы, связанные с управлением пользователей в системе.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Users API")
public class UserController {

    private final UserService userService;

    /**
     * Производит увольнение сотрудника. Использует только владелец фермы.
     *
     * @param employeeID идентификатор сотрудника, подлежащий увольнению
     * @return данные об уволенном сотруднике
     */
    @PutMapping("/{employeeID}")
    @Operation(summary = "Увольнение сотрудника")
    public ResponseUserDto dismissEmployee(@PathVariable("employeeID") Long employeeID) {
        return userService.dismissEmployee(employeeID);
    }
}