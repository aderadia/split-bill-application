package com.hackethon.squad2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hackethon.squad2.dto.request.*;
import com.hackethon.squad2.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BIllService {
    BaseResponseDto<CreateSplitBillResponse> createBill(CreateSplitBillRequest request);
    BaseResponseDto<BillDetailResponse> getDetailBill(BillDetailRequest request);
    BaseResponseDto<BillPayerDetailResponse> getDetailBillPayer(BillPayerDetailRequest request);
    BaseResponseDto<ExecuteBillResponse> executeBill(ExecuteBillRequest request);
    BaseResponseDto<UploadBillResponse> uploadFile(UploadBillRequest request) throws JsonProcessingException;
    BaseResponseDto<UploadBillResponse> uploadFileFromFile(MultipartFile request) throws JsonProcessingException;
    BaseResponseDto<List<GetSplitBillResponse>> getAllSplitBill(GetSplitBillRequest request);
}
