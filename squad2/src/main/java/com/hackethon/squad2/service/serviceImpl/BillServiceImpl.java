package com.hackethon.squad2.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackethon.squad2.constant.BillConstant;
import com.hackethon.squad2.dto.*;
import com.hackethon.squad2.dto.request.*;
import com.hackethon.squad2.dto.response.*;
import com.hackethon.squad2.model.*;
import com.hackethon.squad2.repository.*;
import com.hackethon.squad2.service.BIllService;
import com.hackethon.squad2.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BillServiceImpl implements BIllService {
    @Value(value = "${ocr.service}")
    private String ocrServiceUrl;

    @Value(value = "${google.api}")
    private String googleApiUrl;

    @Value(value = "${api.key}")
    private String apiKey;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MutationRepository mutationRepository;

    @Autowired
    SofRepository sofRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    SplitBillRepository splitBillRepository;

    @Autowired
    SplitBillUserRepository splitBillUserRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    SplitBillItemRepository splitBillItemRepository;

    @Autowired
    SplitBillSubItemRepository splitBillSubItemRepository;

    @Override
    @Transactional
    public BaseResponseDto<CreateSplitBillResponse> createBill(CreateSplitBillRequest request) {
        try {
            var response = new BaseResponseDto<CreateSplitBillResponse>();
/**            insert ke db split bill **/
            var sofOwner = sofRepository.findByAccNum(request.getOwner().getAccNum());
            if (sofOwner == null) {
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                response.setMesage(BillConstant.USER_NOT_FOUND_MESSAGE);
                return response;
            }

            var splitBill = SplitBill.builder()
                    .title(request.getTitle())
                    .sof(sofOwner)
                    .billImg("")
                    .totalAmount(request.getTotalAmount())
                    .subtotalAmount(request.getTotalAmount())
                    .taxAmount(request.getTaxAmount())
                    .serviceAmount(request.getServiceAmount())
                    .discount(request.getDiscount())
                    .others(request.getOthers())
                    .createdAt(LocalDateTime.now())
                    .billId(StringUtils.generateUniqId(BillConstant.PREFIX_BILL_ID))
                    .build();
            SplitBill finalSplitBill = splitBillRepository.save(splitBill);

            /**insert ke db bill user **/
            List<OtherContactDto> users = new ArrayList<>(request.getOtherUsers());
            users.add(request.getOwner());
            var splitBillUserId = StringUtils.generateUniqId(BillConstant.PREFIX_BILL_USER_ID);

            users.forEach(data -> {
                    var sof = sofRepository.findByAccNum(data.getAccNum());
                    saveData(data, finalSplitBill, sof, data.getAccNum().equals(request.getOwner().getAccNum()), splitBillUserId);
                });
            response.setData(CreateSplitBillResponse.builder().billId(String.valueOf(splitBill.getBillId())).build());
            response.setError(BillConstant.SUCCESS_CODE);
            response.setMesage(BillConstant.SUCCESS_MESSAGE);
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponseDto<BillDetailResponse> getDetailBill(BillDetailRequest request) {
        try {
            var response = new BaseResponseDto<BillDetailResponse>();
            var detailBIll = new BillDetailResponse();
            var bill = splitBillRepository.findByBillId(request.getBillId());
            if (bill.isPresent()){
                var splitBill = bill.get();
                 detailBIll.setTitle(splitBill.getTitle());
                 detailBIll.setTotalAmount(splitBill.getTotalAmount());
                 detailBIll.setSubtotalAmount(splitBill.getSubtotalAmount());
                 detailBIll.setTaxAmount(splitBill.getTaxAmount());
                 detailBIll.setServiceAmount(splitBill.getServiceAmount());
                 detailBIll.setDiscount(splitBill.getDiscount());
                 detailBIll.setOthers(splitBill.getOthers());
                 detailBIll.setDate(StringUtils.dateToString(splitBill.getCreatedAt(),"yyyy-MM-dd HH:mm:ss").getDateTime());
                 var billUser = splitBillUserRepository.findBySplitBillId(splitBill.getId());
                 if (!billUser.isEmpty()){
                     var otherUserList = new ArrayList<OtherContactDto>();
                     billUser.forEach(data -> {
                         if (data.getSof().getAccNum().equalsIgnoreCase(splitBill.getSof().getAccNum())){
                             detailBIll.setOwner(OtherContactDto.builder()
                                             .name(data.getSof().getUser().getName())
                                             .accNum(data.getSof().getAccNum())
                                             .amount(data.getAmount())
                                             .items(mappingItems(data.getSplitBillItems()))
                                             .build());

                         }else {
                             otherUserList.add(OtherContactDto.builder()
                                                 .name(data.getSof().getUser().getName())
                                                 .accNum(data.getSof().getAccNum())
                                                 .amount(data.getAmount())
                                                 .items(mappingItems(data.getSplitBillItems()))
                                                 .build());
                         }
                     });
                     detailBIll.setOtherUsers(otherUserList);
                 }
                 response.setData(detailBIll);
                 response.setMesage(BillConstant.SUCCESS_MESSAGE);
                 response.setError(BillConstant.SUCCESS_CODE);
                 return response;
            }
            response.setError(BillConstant.GENERAL_ERROR_CODE);
            response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponseDto<BillPayerDetailResponse> getDetailBillPayer(BillPayerDetailRequest request) {
        try {
            var response = new BaseResponseDto<BillPayerDetailResponse>();
            var sof = sofRepository.findByAccNum(request.getAccNum());
            if (sof==null){
                response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }
            var userBill = splitBillUserRepository.findBySofIdAndBillUserId(sof.getId(), request.getBillUserId());
            if (userBill==null){
                response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }
            response.setError(BillConstant.SUCCESS_CODE);
            response.setMesage(BillConstant.SUCCESS_MESSAGE);
            response.setData(BillPayerDetailResponse.builder()
                            .title(userBill.getSplitBill().getTitle())
                            .totalAmount(userBill.getAmount())
                            .owner(ContactDto.builder()
                                    .name(userBill.getSplitBill().getSof().getUser().getName())
                                    .accNum(userBill.getSplitBill().getSof().getAccNum()).build())
                            .payer(ContactDto.builder()
                                    .name(userBill.getName())
                                    .accNum(userBill.getSof().getAccNum()).build())
                            .items(mappingItems(userBill.getSplitBillItems()))
                            .date(StringUtils.dateToString(userBill.getCreatedAt(),"yyyy-MM-dd HH:mm:ss").getDateTime())
                    .build());
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public BaseResponseDto<ExecuteBillResponse> executeBill(ExecuteBillRequest request) {
        try {
            var response = new BaseResponseDto<ExecuteBillResponse>();
            var sof = sofRepository.findByAccNum(request.getAccNum());
            var ownerSof = sofRepository.findByAccNum(request.getAccDest());
            if (sof==null || ownerSof==null){
                response.setMesage(BillConstant.USER_NOT_FOUND_MESSAGE);
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }

            var billUser = splitBillUserRepository.findBySofIdAndBillUserId(sof.getId(), request.getBillUserId());
            if (billUser==null){
                response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }

            if (billUser.getStatus().equalsIgnoreCase(BillConstant.PAID_STATUS)){
                response.setMesage("Bill has been paid");
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }
            var balance = sof.getBalance().subtract(billUser.getAmount());
            sof.setBalance(balance);
            billUser.setStatus(BillConstant.PAID_STATUS);
            sofRepository.save(sof);
            splitBillUserRepository.save(billUser);

//            Success screen
            response.setMesage(BillConstant.SUCCESS_MESSAGE);
            response.setError(BillConstant.SUCCESS_CODE);
            response.setData(ExecuteBillResponse.builder()
                            .title(billUser.getSplitBill().getTitle())
                            .owner(ContactDto.builder().name(ownerSof.getUser().getName())
                                    .accNum(ownerSof.getAccNum()).build())
                            .amount(billUser.getAmount())
                            .items(mappingItems(billUser.getSplitBillItems()))
                    .build());
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    private List<ItemDto> mappingItems(List<SplitBillItem> items){
        if (items.isEmpty()) return new ArrayList<>();
        return items.stream()
                .map(data -> ItemDto.builder()
                        .name(data.getName())
                        .quantity(data.getQuantity())
                        .price(data.getPrice())
                        .subItems(mappingSubItems(data.getSplitBillSubitems()))
                        .build()).toList();
    }

    private List<SubItem> mappingSubItems(List<SplitBillSubitem> subitems){
        if (subitems.isEmpty()) return new ArrayList<>();
        return subitems.stream()
                .map(data -> SubItem.builder()
                        .name(data.getName())
                        .quantity(data.getQuantity())
                        .price(data.getPrice())
                        .build()).toList();
    }

    @Override
    public BaseResponseDto<UploadBillResponse> uploadFile(UploadBillRequest request) throws JsonProcessingException {

        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestBody = String.format("""
        {
            "fileBase64": "%s"
        }
        """, request.getImageBase64());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> req = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> ocrResponse = restTemplate.postForEntity(
                    ocrServiceUrl,
                    req,
                    String.class
            );
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            try {
                root = mapper.readTree(ocrResponse.getBody());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String rawText = root.get("raw_text").asText();
            log.info("Raw Text : " + rawText);

            String prompt = "Tolong buat hasil ocr ini " + rawText +
                    " format kedalam JSON saja tanpa deskripsi apapun, seperti format berikut: "
                    + "{" +
                    "\"title\" : \"\",\n" +
                    "\"totalAmount\" : ,\n" +
                    "\"subTotalAmount\" : ,\n" +
                    "\"discount\" : ,\n" +
                    "\"others\" : ,\n" +
                    "\"taxAmount\" : ,\n" +
                    "\"serviceAmount\" : ,\n" +
                    "\"items\" : [\n" +
                    "    {\n" +
                    "        \"name\" : \"\",\n" +
                    "        \"quantity\" : \"\",\n" +
                    "        \"price\" : \"\",\n" +
                    "        \"subItems\" : [\n" +
                    "            {\n" +
                    "                \"name\" : \"\",\n" +
                    "                \"quantity\" : \"\",\n" +
                    "                \"price\" : \"\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "]}";


            OpenAiRequest reqOpenAi = new OpenAiRequest(prompt);

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<OpenAiRequest> entity = new HttpEntity<>(reqOpenAi, header);

            ResponseEntity<OpenAiResponse> res = restTemplate.exchange(
                    googleApiUrl.concat(apiKey),
                    HttpMethod.POST,
                    entity,
                    OpenAiResponse.class
            );

            OpenAiResponse bodi = res.getBody();
            if (bodi != null && !bodi.candidates.isEmpty()) {
                var text = bodi.candidates.get(0).content.parts.get(0).text;
                int start = text.indexOf('{');
                int end = text.lastIndexOf('}');

                if (start != -1 && end != -1 && end > start) {
                    String json = text.substring(start, end + 1);
                    ObjectMapper map = new ObjectMapper();
                    UploadBillResponse uploadBillResponse = map.readValue(json, UploadBillResponse.class);
                    return BaseResponseDto.<UploadBillResponse>builder()
                            .data(uploadBillResponse)
                            .build();
                } else {
                    return new BaseResponseDto<UploadBillResponse>();
                }
            } else {
                return new BaseResponseDto<>();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponseDto<UploadBillResponse> uploadFileFromFile(MultipartFile request) throws JsonProcessingException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", request.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(ocrServiceUrl, HttpMethod.POST, requestEntity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            try {
                root = mapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String rawText = root.get("raw_text").asText();
            log.info("Raw Text : " + rawText);

            String prompt = "Tolong buat hasil ocr ini " + rawText +
                    " format kedalam JSON saja tanpa deskripsi apapun, seperti format berikut: "
                    + "{" +
                    "\"title\" : \"\",\n" +
                    "\"totalAmount\" : ,\n" +
                    "\"subTotalAmount\" : ,\n" +
                    "\"discount\" : ,\n" +
                    "\"others\" : ,\n" +
                    "\"taxAmount\" : ,\n" +
                    "\"serviceAmount\" : ,\n" +
                    "\"items\" : [\n" +
                    "    {\n" +
                    "        \"name\" : \"\",\n" +
                    "        \"quantity\" : \"\",\n" +
                    "        \"price\" : \"\",\n" +
                    "        \"subItems\" : [\n" +
                    "            {\n" +
                    "                \"name\" : \"\",\n" +
                    "                \"quantity\" : \"\",\n" +
                    "                \"price\" : \"\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "]}";


            OpenAiRequest req = new OpenAiRequest(prompt);

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<OpenAiRequest> entity = new HttpEntity<>(req, header);

            ResponseEntity<OpenAiResponse> res = restTemplate.exchange(
                    googleApiUrl.concat(apiKey),
                    HttpMethod.POST,
                    entity,
                    OpenAiResponse.class
            );

            OpenAiResponse bodi = res.getBody();
            if (bodi != null && !bodi.candidates.isEmpty()) {
                var text = bodi.candidates.get(0).content.parts.get(0).text;
                int start = text.indexOf('{');
                int end = text.lastIndexOf('}');

                if (start != -1 && end != -1 && end > start) {
                    String json = text.substring(start, end + 1);
                    ObjectMapper map = new ObjectMapper();
                    UploadBillResponse uploadBillResponse = map.readValue(json, UploadBillResponse.class);
                    return BaseResponseDto.<UploadBillResponse>builder()
                            .data(uploadBillResponse)
                            .build();
                } else {
                    return new BaseResponseDto<UploadBillResponse>();
                }
            } else {
                return new BaseResponseDto<>();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponseDto<List<GetSplitBillResponse>> getAllSplitBill(GetSplitBillRequest request) {
        try {
            var response = new BaseResponseDto<List<GetSplitBillResponse>>();
            var sof = sofRepository.findByAccNum(request.getAccNum());

            if (sof==null){
                response.setMesage(BillConstant.USER_NOT_FOUND_MESSAGE);
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }
            var splitBills = splitBillRepository.findBySofId(sof.getId());
            if (splitBills.isEmpty()){
                response.setMesage(BillConstant.DATA_NOT_FOUND_MESSAGE);
                response.setError(BillConstant.GENERAL_ERROR_CODE);
                return response;
            }

            response.setMesage(BillConstant.SUCCESS_MESSAGE);
            response.setError(BillConstant.SUCCESS_CODE);
            response.setData(splitBills.stream()
                    .map(data-> GetSplitBillResponse.builder()
                            .title(data.getTitle())
                            .totalAmount(data.getTotalAmount())
                            .totalPeople(data.getSplitBillUsers().size())
                            .date(StringUtils.dateToString(data.getCreatedAt(),"yyyy-MM-dd HH:mm:ss").getDate())
                            .build()).toList());
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData(OtherContactDto req, SplitBill splitBIllId, Sof sof, Boolean isOwner, String billUserId){

        var splitBillUser = SplitBillUser.builder()
                .name(req.getName())
                .amount(req.getAmount())
                .createdAt(splitBIllId.getCreatedAt())
                .splitBill(splitBIllId)
                .status(isOwner? BillConstant.PAID_STATUS : BillConstant.UNPAID_STATUS)
                .sof(sof)
                .billUserId(billUserId)
                .build();

        SplitBillUser finalSplitBillUser = splitBillUserRepository.save(splitBillUser);

        if (!isOwner){
            var notifRequest = com.hackethon.squad2.model.Notification.builder()
                    .user(sof.getUser())
                    .createdAt(LocalDateTime.now())
                    .description("") // isi deskripsi sesuai UI
                    .productCode(BillConstant.SPLIT_BILL_CODE) // isi productCode
                    .title("") // sesuain sama UI
                    .splitBillUser(finalSplitBillUser)
                    .build();
            notificationRepository.save(notifRequest);
        }

        /** insert ke items dan subItems **/
        req.getItems().forEach(data -> {
            var item = SplitBillItem.builder()
                    .name(data.getName())
                    .splitBillUser(finalSplitBillUser)
                    .splitBillId(splitBIllId)
                    .quantity(data.getQuantity())
                    .price(data.getPrice())
                    .createdAt(LocalDateTime.now())
                    .build();
            SplitBillItem splitBillItem = splitBillItemRepository.save(item);
                if (data.getSubItems()!=null){
                    List<SplitBillSubitem> subItemList = data.getSubItems().stream()
                            .map(subData ->
                                    SplitBillSubitem.builder()
                                    .name(subData.getName())
                                    .price(subData.getPrice())
                                    .quantity(subData.getQuantity())
                                    .splitBillItem(splitBillItem)
                                    .build()).toList();
                    splitBillSubItemRepository.saveAll(subItemList);
                }
        });
    }
}
