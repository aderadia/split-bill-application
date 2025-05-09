package com.hackethon.squad2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDto implements Serializable {
    private static final long serialVersionUID= 9144414777410039780L;
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalData;
}
