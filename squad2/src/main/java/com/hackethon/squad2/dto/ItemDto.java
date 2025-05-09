package com.hackethon.squad2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {
    private static final long serialVersionUID= 469145524580039599L;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private List<SubItem> subItems;
}
