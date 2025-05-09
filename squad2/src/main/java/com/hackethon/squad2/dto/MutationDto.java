package com.hackethon.squad2.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MutationDto implements Serializable {
    private static final long serialVersionUID= 4573646444589020891L;
    private BigDecimal balanceOut;
    private String description;
    private String accounNumber;
    private String transactionDate;
    private String productCode;
    private String title;
}
