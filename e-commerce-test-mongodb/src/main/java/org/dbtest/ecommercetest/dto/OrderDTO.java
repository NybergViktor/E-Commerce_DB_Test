package org.dbtest.ecommercetest.dto;

import lombok.Data;
import org.dbtest.ecommercetest.model.Product;

import java.util.List;

@Data
public class OrderDTO {
    public String id;
    public List<String> products;
    public String buyer_id;
}
