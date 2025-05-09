package com.hackethon.squad2.dto;

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
public class SofDto implements Serializable {
    private static final long serialVersionUID = 7545786426326849443L;

    private String accNum;
    private BigDecimal balance;
    private String productCode;
    private UserDto userDto;
}
