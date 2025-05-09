package com.hackethon.squad2.dto.response;

import com.hackethon.squad2.dto.OtherContactDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.MediaSize;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailResponse implements Serializable {
    private static final long serialVersionUID= 8111821693673742484L;
    private String title;
    private BigDecimal totalAmount;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal serviceAmount;
    private BigDecimal discount;
    private BigDecimal others;
    private String date;
    private List<OtherContactDto> otherUsers;
    private OtherContactDto owner;
}
