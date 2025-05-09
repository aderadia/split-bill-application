package com.hackethon.squad2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ContactDto implements Serializable {
    private static final long serialVersionUID= 8434913038293675591L;
    private String name;
    @JsonProperty("accounNumber")
    private String accNum;
}
