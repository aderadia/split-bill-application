package com.hackethon.squad2.util;

import com.hackethon.squad2.constant.BillConstant;
import com.hackethon.squad2.dto.DateTimeDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class StringUtils {

    public static DateTimeDto dateToString(LocalDateTime localDateTime, String format){
        var dtf = DateTimeFormatter.ofPattern(format);
        String[] dateTime = localDateTime.format(dtf).split(" ");
        return DateTimeDto.builder()
                .date(dateTime[0])
                .time(dateTime[1])
                .dateTime(localDateTime.format(dtf))
                 .build();
    }

    public static String generateUniqId(String prefix){
        Random random = new Random();
        return prefix.concat(dateToString(LocalDateTime.now(),"ddMMyyyyhhmmss").getDateTime())
                .concat(String.valueOf(random.nextInt(90000)+10000));
    }
}
