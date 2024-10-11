package com.Ecommerce_backend.Filter;

import com.Ecommerce_backend.entity.Product;
//import com.Ecommerce_backend.entity.Review;
import jakarta.persistence.criteria.*;
import jdk.jfr.Category;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class ProductSpec {
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY = "category";
    public static final String PRODUCT_PRICE = "productPrice";

    private ProductSpec() {

    }

    public static Specification<Product> filterByCategory(String category) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.isMember(category, root.get("categories"));
        };
    }

    public static Specification<Product> filterByCategories(List<String> categories) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return criteriaBuilder.conjunction(); // No filtering if categories list is empty or null
            }

            // Join the product with its categories
            Join<Product, Category> categoryJoin = root.join("categories", JoinType.INNER);

            // Create a predicate for the category names in the provided list
            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(categoryJoin.get("name"));
            for (String category : categories) {
                inClause.value(category);
            }

            return inClause;
        };
    }


    public static Specification<Product> filterByPriceRange(BigDecimal priceFrom , BigDecimal priceTo) {
        return Specification
                .where(hasPriceGreaterThan(priceFrom))
                .and(hasPriceLessThan(priceTo));
    }

    public static Specification<Product> filterByPriceRangeAndCategory(BigDecimal priceFrom , BigDecimal priceTo, List<String> categories) {
        return Specification
                .where(hasPriceGreaterThan(priceFrom))
                .and(hasPriceLessThan(priceTo)).and(filterByCategories(categories));
    }



    public static Specification<Product> searchByTitleDescription(ProductFilter productFilter) {
        return Specification
                .where(likeTitle(productFilter.getTitle()))
                .and(likeDescription(productFilter.getDescription()));
    }

    public static Specification<Product> searchByTitle(ProductFilter productFilter) {
        return Specification
                .where(likeTitle(productFilter.getTitle()));
    }

    public static Specification<Product> searchByDescription(ProductFilter productFilter) {
        return Specification
                .where(likeDescription(productFilter.getDescription()));
    }


    private static Specification<Product> hasPriceGreaterThan(BigDecimal priceFrom) {
        return (root, query, cb) -> priceFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get(PRODUCT_PRICE), priceFrom);
    }

    private static Specification<Product> hasPriceLessThan(BigDecimal priceTo) {
        return (root, query, cb) -> priceTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get(PRODUCT_PRICE), priceTo);
    }

    private static Specification<Product> likeTitle(String title) {
        return (root, query, cb) -> title == null ? cb.conjunction() : cb.like(root.get(TITLE), "%" + title + "%");
    }

    private static Specification<Product> likeDescription(String description) {
        return (root, query, cb) -> description == null ? cb.conjunction() : cb.like(root.get(DESCRIPTION),"%" +  description + "%");
    }


}
