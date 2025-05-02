package org.dbtest.ecommercetest.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private List<Product> products;
    private double total_price;
    private int amount_of_products;
    private String buyer_id;

}
