package com.hackethon.squad2.controller.login;

import com.hackethon.squad2.dto.request.*;
import com.hackethon.squad2.dto.response.*;
import com.hackethon.squad2.service.BIllService;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    BIllService bIllService;

    @PostMapping("/create-split-bill-from-mutation")
    public BaseResponseDto<CreateSplitBillResponse> createBillFromMutation(@RequestBody CreateSplitBillRequest request){
        return bIllService.createBill(request);
    }

    @PostMapping("/get-detail-split-bill")
    public BaseResponseDto<BillDetailResponse> getDetailBill(@RequestBody BillDetailRequest request){
        return bIllService.getDetailBill(request);
    }

    @PostMapping("/get-detail-split-bill-payer")
    public BaseResponseDto<BillPayerDetailResponse> getDetailBillPayer(@RequestBody BillPayerDetailRequest request){
        return bIllService.getDetailBillPayer(request);
    }

    @PostMapping("/execute-split-bill")
    public BaseResponseDto<ExecuteBillResponse> executeSplitBill(@RequestBody ExecuteBillRequest request){
        return bIllService.executeBill(request);
    }

    @PostMapping("/execute-bill-image")
    public BaseResponseDto<UploadBillResponse> extractText(@RequestBody UploadBillRequest request) throws IOException {
        return bIllService.uploadFile(request);
    }

    @PostMapping("/execute-bill-image-from-File")
    public BaseResponseDto<UploadBillResponse> extractTextFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        return bIllService.uploadFileFromFile(file);
    }

    @PostMapping("/get-my-split-bill")
    public BaseResponseDto<List<GetSplitBillResponse>> getAllSplitBill(@RequestBody GetSplitBillRequest request) throws IOException {
        return bIllService.getAllSplitBill(request);
    }

}
