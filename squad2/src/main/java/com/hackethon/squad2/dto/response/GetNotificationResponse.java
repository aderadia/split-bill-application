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
public class GetNotificationResponse implements Serializable {
    private static final long serialVersionUID= -4981727305302700675L;
    private String title;
    private String description;
    private String productCode;
    private String billUserId;
    private String date;
}
