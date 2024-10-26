package com.tikjuti.bus_ticket_booking.Utils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class QueryableExtensions {

    public static <T> Specification<T> applyIncludes(String includes) {
        return (root, query, criteriaBuilder) -> {
            if (includes == null || includes.isEmpty()) return criteriaBuilder.conjunction();
            String[] includesArray = includes.split(",");

            for (String include : includesArray) {
                String trimmedInclude = include.trim();
                if (trimmedInclude.isEmpty()) continue;

                String[] propertyNamesArray = trimmedInclude.split("\\.");
                for (String propertyName : propertyNamesArray) {
                    Join<Object, Object> join = (Join<Object, Object>) root;
                    join.fetch(propertyName, JoinType.LEFT);
                }
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static <T> Specification<T> applyFilters(Map<String, Object> filters) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                String key = filter.getKey();
                Object value = filter.getValue();

                if (value instanceof String && ((String) value).contains(":")) {
                    String filterString = (String) value;
                    int firstColonIndex = filterString.indexOf(":");

                    String operatorType = filterString.substring(0, firstColonIndex);
                    String filterValue = filterString.substring(firstColonIndex + 1);

                    Path<Object> path = root.get(key);

                    Object convertedValue = convertValue(path, filterValue);

                    switch (operatorType) {
                        case "eq":
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(path, convertedValue));
                            break;
                        case "neq":
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(path, convertedValue));
                            break;
                        case "gt":
                            if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(
                                        path.as(getPathType(path)), (Comparable) convertedValue));
                            }
                            break;
                        case "gte":
                            if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(
                                        path.as(getPathType(path)), (Comparable) convertedValue));
                            }
                            break;
                        case "lt":
                            if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(
                                        path.as(getPathType(path)), (Comparable) convertedValue));
                            }
                            break;
                        case "lte":
                            if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(
                                        path.as(getPathType(path)), (Comparable) convertedValue));
                            }
                            break;
                        case "like":
                            if (path.getJavaType().equals(String.class)) {
                                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                                        path.as(String.class), "%" + filterValue + "%"));
                            }
                            break;
                        case "in":
                            String[] values = filterValue.split(",");
                            predicate = criteriaBuilder.and(predicate, path.in(List.of(values)));
                            break;
                        default:
                            throw new RuntimeException("Invalid filter operator: " + operatorType);
                    }
                } else if (value != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(key), value));
                }
            }
            return predicate;
        };
    }

    private static Object convertValue(Path<?> path, String value) {
        Class<?> type = path.getJavaType();
        if (type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(LocalDate.class)) {
            return LocalDate.parse(value);
        } else if (type.equals(LocalTime.class)) {
            return LocalTime.parse(value);
        }
        return value;
    }

    private static Class<? extends Comparable> getPathType(Path<?> path) {
        Class<?> type = path.getJavaType();
        if (Comparable.class.isAssignableFrom(type)) {
            return (Class<? extends Comparable>) type;
        }
        throw new IllegalArgumentException("Path type is not Comparable: " + type);
    }



    public static <T> Specification<T> applySorting(String sortExpression) {
        return (root, query, criteriaBuilder) -> {
            if (sortExpression == null || sortExpression.isEmpty()) return null;

            String[] sortExpressions = sortExpression.split(",");
            for (String expression : sortExpressions) {
                String trimmedExpression = expression.trim();
                boolean isDescending = trimmedExpression.startsWith("-");
                String propertyName = isDescending ? trimmedExpression.substring(1) : trimmedExpression;

                if (isDescending) {
                    query.orderBy(criteriaBuilder.desc(root.get(propertyName)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(propertyName)));
                }
            }
            return query.getRestriction();
        };
    }

    public static <T> PaginatedResult<T> applyPagination(JpaSpecificationExecutor<T> repository, Specification<T> spec, int page, int pageSize) {
        long totalItems = repository.count(spec);
        List<T> items = repository.findAll(spec, PageRequest.of(page - 1, pageSize)).getContent();

        return new PaginatedResult<>(items, (int) totalItems, page, pageSize);
    }
}