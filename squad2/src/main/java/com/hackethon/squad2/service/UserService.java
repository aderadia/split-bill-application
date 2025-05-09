package com.hackethon.squad2.service;

import com.hackethon.squad2.dto.ContactDto;
import com.hackethon.squad2.dto.MutationDto;
import com.hackethon.squad2.dto.UserDto;
import com.hackethon.squad2.dto.request.GetContactRequest;
import com.hackethon.squad2.dto.request.GetMutationRequest;
import com.hackethon.squad2.dto.request.GetNotificationRequest;
import com.hackethon.squad2.dto.response.BaseResponseDto;
import com.hackethon.squad2.dto.response.GetNotificationResponse;
import com.hackethon.squad2.dto.response.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    PaginationResponse<MutationDto> getAllMutationsPagination(GetMutationRequest request, Integer page, Integer size);
    BaseResponseDto<List<MutationDto>> getAllMutations(GetMutationRequest request);
    BaseResponseDto<List<ContactDto>> getAllMyContact(GetContactRequest request);
    BaseResponseDto<List<GetNotificationResponse>> getNotification(GetNotificationRequest request);
}
