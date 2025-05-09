package com.hackethon.squad2.dto.response;

import com.hackethon.squad2.dto.PaginationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> implements Serializable {
    private static final long serialVersionUID = 7601587608275537636L;
    private List<T> data;
    private PaginationDto metaData;
}
