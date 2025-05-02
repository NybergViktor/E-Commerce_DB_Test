package org.dbtest.ecommercetest.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;

import java.util.List;

@Data
public class User {
    @Id
    private String id;
    private String name;
    private List<String> userOrders;


}
