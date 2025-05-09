package com.hackethon.squad2.dto.request;

import com.hackethon.squad2.dto.OtherContactDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSplitBillRequest {
    private String title;
    private BigDecimal totalAmount;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal serviceAmount;
    private BigDecimal discount;
    private BigDecimal others;
    private List<OtherContactDto> otherUsers;
    private OtherContactDto owner;
}
