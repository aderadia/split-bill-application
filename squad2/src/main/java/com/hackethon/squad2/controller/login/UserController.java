package com.hackethon.squad2.controller.login;

import com.hackethon.squad2.dto.ContactDto;
import com.hackethon.squad2.dto.MutationDto;
import com.hackethon.squad2.dto.request.BillDetailRequest;
import com.hackethon.squad2.dto.request.GetContactRequest;
import com.hackethon.squad2.dto.request.GetMutationRequest;
import com.hackethon.squad2.dto.request.GetNotificationRequest;
import com.hackethon.squad2.dto.response.BaseResponseDto;
import com.hackethon.squad2.dto.response.BillDetailResponse;
import com.hackethon.squad2.dto.response.GetNotificationResponse;
import com.hackethon.squad2.dto.response.PaginationResponse;
import com.hackethon.squad2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/get-my-mutations-paginantion")
    public PaginationResponse<MutationDto> getMutationPagination(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestBody GetMutationRequest request){
        return userService.getAllMutationsPagination(request, page, size);
    }

    @PostMapping("/get-my-mutations")
    public BaseResponseDto<List<MutationDto>> getMutation(@RequestBody GetMutationRequest request){
        return userService.getAllMutations(request);
    }

    @PostMapping("/get-my-contact")
    public BaseResponseDto<List<ContactDto>> getMutation(@RequestBody GetContactRequest request){
        return userService.getAllMyContact(request);
    }

    @PostMapping("/get-my-notification")
    public BaseResponseDto<List<GetNotificationResponse>> getNotification(@RequestBody GetNotificationRequest request){
        return userService.getNotification(request);
    }
}
