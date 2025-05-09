package com.hackethon.squad2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDto<T> implements Serializable {
    private static final long serialVersionUID = 7305894018326620114L;
    private T data;
    private String mesage;
    private String error;
}
