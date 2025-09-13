package com.mechtrack.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenericQuerySpec {

    public static <T> Specification<T> build(QueryBuilder<T> builder) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            builder.build(root, query, cb, predicates);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> void addStringFilter(Root<T> root, CriteriaBuilder cb, List<Predicate> predicates, 
                                          String fieldName, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(cb.like(cb.upper(root.get(fieldName)), "%" + value.toUpperCase() + "%"));
        }
    }

    public static <T, Y> void addStringFilter(Join<T, Y> join, CriteriaBuilder cb, List<Predicate> predicates, 
                                             String fieldName, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(cb.like(cb.upper(join.get(fieldName)), "%" + value.toUpperCase() + "%"));
        }
    }

    public static <T> void addDateRangeFilter(Root<T> root, CriteriaBuilder cb, List<Predicate> predicates,
                                             String fieldName, LocalDate startDate, LocalDate endDate) {
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), endDate));
        }
    }

    public static <T, Y> void addDateRangeFilter(Join<T, Y> join, CriteriaBuilder cb, List<Predicate> predicates,
                                                String fieldName, LocalDate startDate, LocalDate endDate) {
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(join.get(fieldName), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(join.get(fieldName), endDate));
        }
    }

    public static <T> void addDecimalRangeFilter(Root<T> root, CriteriaBuilder cb, List<Predicate> predicates,
                                                String fieldName, BigDecimal minValue, BigDecimal maxValue) {
        if (minValue != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), minValue));
        }
        if (maxValue != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), maxValue));
        }
    }

    public static <T, Y> void addDecimalRangeFilter(Join<T, Y> join, CriteriaBuilder cb, List<Predicate> predicates,
                                                   String fieldName, BigDecimal minValue, BigDecimal maxValue) {
        if (minValue != null) {
            predicates.add(cb.greaterThanOrEqualTo(join.get(fieldName), minValue));
        }
        if (maxValue != null) {
            predicates.add(cb.lessThanOrEqualTo(join.get(fieldName), maxValue));
        }
    }

    @FunctionalInterface
    public interface QueryBuilder<T> {
        void build(Root<T> root, jakarta.persistence.criteria.CriteriaQuery<?> query, 
                  CriteriaBuilder cb, List<Predicate> predicates);
    }
}
