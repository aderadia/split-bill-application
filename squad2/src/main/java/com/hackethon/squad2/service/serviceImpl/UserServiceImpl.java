package com.hackethon.squad2.service.serviceImpl;

import com.hackethon.squad2.constant.BillConstant;
import com.hackethon.squad2.dto.*;
import com.hackethon.squad2.dto.request.GetContactRequest;
import com.hackethon.squad2.dto.request.GetMutationRequest;
import com.hackethon.squad2.dto.request.GetNotificationRequest;
import com.hackethon.squad2.dto.response.BaseResponseDto;
import com.hackethon.squad2.dto.response.GetNotificationResponse;
import com.hackethon.squad2.dto.response.PaginationResponse;
import com.hackethon.squad2.model.Mutations;
import com.hackethon.squad2.model.Sof;
import com.hackethon.squad2.repository.*;
import com.hackethon.squad2.service.UserService;
import com.hackethon.squad2.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MutationRepository mutationRepository;

    @Autowired
    SofRepository sofRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public PaginationResponse<MutationDto> getAllMutationsPagination(GetMutationRequest request, Integer page, Integer size) {
        try {

            Pageable pageable = PageRequest.of(page,size);
            Page<Mutations> mutations = mutationRepository.findAllBySofPage(request.getAccNum(), pageable);
            List<MutationDto> mutationList = mutations.getContent()
                    .stream()
                    .map(data -> MutationDto.builder()
                            .balanceOut(data.getBalanceOut())
                            .description(data.getDescription())
                            .transactionDate(StringUtils.dateToString(data.getCreatedAt(), "dd-MM-yyyy HH:mm:ss").getDateTime())
                            .accounNumber(data.getSof().getAccNum())
                            .productCode(data.getProductCode())
                            .build()).toList();
            return new PaginationResponse<MutationDto>(
                    mutationList,
                    PaginationDto.builder()
                            .page(mutations.getNumber())
                            .size(mutations.getSize())
                            .totalPage(mutations.getTotalPages())
                            .totalData(mutations.getTotalElements()).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponseDto<List<MutationDto>> getAllMutations(GetMutationRequest request) {
        try {
            var response = new BaseResponseDto<List<MutationDto>>();
            var mutations = mutationRepository.findAllBySof(request.getAccNum());
            if (mutations.isEmpty()){
              response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
              response.setError(BillConstant.GENERAL_ERROR_CODE);
              return response;
            }
            response.setData(mutations.stream()
                    .map(data-> MutationDto.builder()
                            .balanceOut(data.getBalanceOut())
                            .description(data.getDescription())
                            .productCode(data.getProductCode())
                            .transactionDate(StringUtils.dateToString(data.getCreatedAt(), "dd-MM-yyyy HH:mm:ss").getDateTime())
                            .title(data.getTitle())
                            .build()).toList());
            response.setMesage(BillConstant.SUCCESS_MESSAGE);
            response.setError(BillConstant.SUCCESS_CODE);
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BaseResponseDto<List<ContactDto>> getAllMyContact(GetContactRequest request) {
        try {
            var response = new BaseResponseDto<List<ContactDto>>();
            var contactList = contactRepository.findAllBySofId(getUser(request.getAccNum()));
            if (contactList.isEmpty()){
                response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }
            response.setData(contactList
                    .stream()
                    .map(data -> ContactDto.builder()
                            .accNum(data.getAccNum())
                            .name(data.getName())
                            .build()).toList());
            response.setMesage(BillConstant.SUCCESS_MESSAGE);
            response.setError(BillConstant.SUCCESS_CODE);
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponseDto<List<GetNotificationResponse>> getNotification(GetNotificationRequest request) {
        try {
            var response = new BaseResponseDto<List<GetNotificationResponse>>();
            var user = userRepository.findByPhoneNumber(request.getPhoneNumber());
            if (user==null){
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                response.setMesage(BillConstant.USER_NOT_FOUND_MESSAGE);
                return response;
            }

            var notif = notificationRepository.findByUserId(user.getId());
            if (notif.isEmpty()) {
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
                return response;
            }

            response.setData(notif.stream().map(data -> GetNotificationResponse.builder()
                    .title(data.getTitle())
                    .description(data.getDescription())
                    .productCode(data.getProductCode())
                    .billUserId(String.valueOf(data.getSplitBillUser().getId()))
                    .date(StringUtils.dateToString(data.getCreatedAt(),"yyyy-MM-dd HH:mm:ss").getDateTime())
                    .build()).toList());
            response.setMesage(BillConstant.SUCCESS_MESSAGE);
            response.setError(BillConstant.SUCCESS_CODE);
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserDto> getAllUser(){
        var response = new ArrayList<UserDto>();
        return userRepository.findAll().stream().map(data -> UserDto.builder()
                .userId(String.valueOf(data.getId()))
                .name(data.getName())
                .sof(mappingSof(data.getSofList()))
                .build()).toList();
    }

    private List<SofDto> mappingSof(List<Sof> sofs){
        return sofs.stream().map(data -> SofDto.builder()
                .accNum(data.getAccNum())
                .balance(data.getBalance())
                .productCode(data.getProductCode())
                .build()).toList();
    }

    private UUID getUser(String accNum){
        var sof = sofRepository.findByAccNum(accNum);

        if (sof!=null){
            return sof.getUser().getId();
        }else {
         return null;
        }
    }

}
