package com.example.e_commerce_test_db.repositories;

import com.example.e_commerce_test_db.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> advancedSearch(String name, double minPrice, double maxPrice, String sortBy, boolean ascending) {
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max");

        if (name != null && !name.isBlank()) {
            queryBuilder.append(" AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "price";
        }

        queryBuilder.append(" ORDER BY p." + sortBy + (ascending ? " ASC" : " DESC"));

        TypedQuery<Product> query = entityManager.createQuery(queryBuilder.toString(), Product.class);
        query.setParameter("min", minPrice);
        query.setParameter("max", maxPrice);

        if (name != null && !name.isBlank()) {
            query.setParameter("name", name);
        }

        return query.getResultList();
    }
}

