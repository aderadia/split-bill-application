package com.hackethon.squad2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetSplitBillResponse implements Serializable {
    private static final long serialVersionUID = -3657396332679821463L;
    private String title;
    private BigDecimal totalAmount;
    private Integer totalPeople;
    private String date;
}
