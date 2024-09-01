package com.assesment.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    public static String localDateTimeToString(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }
        String formattedDateTime = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedDateTime = localDateTime.format(formatter);

        return formattedDateTime;
    }
}
