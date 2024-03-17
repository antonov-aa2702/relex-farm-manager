package ru.relex.backend.aop.pointcut;

import ru.relex.backend.aop.annotation.Loggable;

/**
 * Класс для определения точек среза в программе.
 */
public class Pointcut {

    /**
     * Определяет все классы, которые находятся в пакете ru.relex.backend.service.impl.
     */
    @org.aspectj.lang.annotation.Pointcut("within(ru.relex.backend.service.impl.*)")
    public void isServiceLayer() {
    }


    /**
     * Определяет все такие методы, которые находятся в пакете ru.relex.backend.service.impl и
     * имеют аннотацию {@link Loggable}. Вызовы этих методов должны быть логгированы.
     * <p>
     * Добавление условия {@link  Pointcut#isServiceLayer()} сделано для повышения производительности,
     * чтобы избежать полного сканирования всех методов в программе, так как логирование на начальном этапе разработки
     * рассчитано только на сервисный слой.
     */
    @org.aspectj.lang.annotation.Pointcut("isServiceLayer() && @annotation(ru.relex.backend.aop.annotation.Loggable)")
    public void callLoggableServiceLayerMethod() {
    }
}
