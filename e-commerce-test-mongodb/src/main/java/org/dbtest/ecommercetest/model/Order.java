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
    private List<String> products;
    private String buyer_id;

}
