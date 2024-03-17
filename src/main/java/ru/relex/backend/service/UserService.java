package ru.relex.backend.service;


import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.User;
import ru.relex.backend.exception.ResourceIllegalStateException;

/**
 * Предоставляет методы для работы с пользователями
 */
public interface UserService {

    /**
     * Осуществляет поиск пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     * @throws ResourceIllegalStateException если пользователь не найден
     */
    UserDto getById(Long id) throws ResourceIllegalStateException;

    /**
     * Осуществляет поиск пользователя по электронной почте
     *
     * @param email электронная почта пользователя
     * @return найденный пользователь
     * @throws ResourceIllegalStateException если пользователь не найден
     */
    User getByEmail(String email) throws ResourceIllegalStateException;

    /**
     * Осуществляет увольнение сотрудника. Это достигается
     * путем перевода статуса его аккаунта в "неактивный".
     * <p>
     * Запрещено повторно увольнять уже уволенных сотрудников и запрещено увольнять самого себя,
     * а также не допускается увольнять несуществующих сотрудников. При увольнении сотрудника
     * связанные с ним данные, например, произведенные продукты не удаляются из системы,
     * поскольку могут быть использованы при получении статистических и аналитических отчетов за
     * такой промежуток времени, когда сотрудник еще не был уволен. В дальнейшем можно будет добавить
     * возможность восстановления уволенного сотрудника.
     *
     * @param employeeID идентификатор сотрудника
     * @return данные об уволенном сотруднике
     * @throws ResourceIllegalStateException если сотрудник не найден или уже был уволен или не является сотрудником
     */
    ResponseUserDto dismissEmployee(Long employeeID) throws ResourceIllegalStateException;

}
