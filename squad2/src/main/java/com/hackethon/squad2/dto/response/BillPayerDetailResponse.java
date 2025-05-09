package com.hackethon.squad2.dto.response;

import com.hackethon.squad2.dto.ContactDto;
import com.hackethon.squad2.dto.ItemDto;
import com.hackethon.squad2.dto.OtherContactDto;
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
public class BillPayerDetailResponse implements Serializable {
    private static final long serialVersionUID= 8111821693673742484L;
    private String title;
    private BigDecimal totalAmount;
    private List<ItemDto> items;
    private ContactDto owner;
    private ContactDto payer;
    private String date;
}
