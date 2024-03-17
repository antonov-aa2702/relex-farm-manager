package ru.relex.backend.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, определяющая общие настройки для всех интеграционных тестов.
 * В качестве тестовой базы данных используется Postgres, поднятая
 * с помощью интеграции с TestContainers. Конфигурация базы данных
 * определяется в файле application-test.yml.
 * <p>
 * Это сделано в целях отделения окружения разработки от окружения тестирования.
 * Данный подход позволяет поднимать полноценную, в отличии, например от H2, базу данных,
 * делая процесс тестирования более удобным, качественным и полноценным.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
public @interface IntegrationTest {
}
