package com.hackethon.squad2.dto.response;

import com.hackethon.squad2.dto.ItemDto;
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
public class UploadBillResponse implements Serializable {
    private static final long serialVersionUID= 562957884309680800L;

    private String title;
    private BigDecimal totalAmount;
    private BigDecimal subTotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal serviceAmount;
    private BigDecimal discount;
    private BigDecimal others;
    private List<ItemDto> items;
}
