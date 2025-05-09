package com.hackethon.squad2.dto.response;

import com.hackethon.squad2.dto.ContactDto;
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
public class GetContactResponse implements Serializable {
    private static final long serialVersionUID= -2973717081878153981L;
    private List<ContactDto> data;
}
