package com.hackethon.squad2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadBillRequest {
    private String imageBase64;
    @JsonProperty("accountNumber")
    private String accNum;
}
