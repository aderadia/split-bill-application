package com.hackethon.squad2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
public class SubItem implements Serializable {
    private static final long serialVersionUID= 2869167659947446711L;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    public SubItem(String name, Integer quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
