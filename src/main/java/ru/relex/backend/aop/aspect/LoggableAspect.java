package ru.relex.backend.aop.aspect;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Аспект для логгирования методов.
 */
@Slf4j
@Aspect
@Component
public class LoggableAspect {

    /**
     * Оборачивает вызовы методов, соответствующие точке среза.
     * Он регистрирует начало и конец выполнения метода, а также время его выполнения.
     * Зарегистрированные данные записываются в лог.
     *
     * @param joinPoint объект, представляющий точку среза
     * @return результат выполнения оборачиваемого метода
     */
    @SneakyThrows
    @Around("ru.relex.backend.aop.pointcut.Pointcut.callLoggableServiceLayerMethod()")
    public Object loggableMethodAdvice(ProceedingJoinPoint joinPoint) {
        String methodName = (joinPoint.getSignature()).getName();
        log.info(String.format("Logging method execution '%s'", methodName));
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long totalExecutionTime = System.currentTimeMillis() - start;
        log.info(String.format("Logging method '%s' finished. Execution time: %s ms", methodName, totalExecutionTime));
        return result;
    }
}
