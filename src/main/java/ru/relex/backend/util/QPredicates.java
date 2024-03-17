package ru.relex.backend.util;


import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

/**
 * Класс для формирования предиката для объектов QueryDSL.
 */
@NoArgsConstructor(access = PRIVATE)
public class QPredicates {

    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicates builder() {
        return new QPredicates();
    }

    public <T> QPredicates add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAnd() {
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }
}
