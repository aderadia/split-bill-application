package com.hackethon.squad2.dto.response;

import com.hackethon.squad2.dto.ContactDto;
import com.hackethon.squad2.dto.ItemDto;
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
public class ExecuteBillResponse {
    private String title;
    private BigDecimal amount;
    private List<ItemDto> items;
    private ContactDto owner;
}
