package org.dbtest.ecommercetest.repository;

import org.dbtest.ecommercetest.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Product> search(String name, double minPrice, double maxPrice, String sortBy, boolean ascending) {
        Query query = new Query();

        // Filters
        query.addCriteria(Criteria.where("price").gte(minPrice).lte(maxPrice));
        if (name != null && !name.isBlank()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }


        // Sorting
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(Sort.by(direction, sortBy));

        return mongoTemplate.find(query, Product.class);
    }
}
