package com.jhyx.halfroom.commons;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class LocalDateTimeUtil {
    private static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd";
    private static final String DEFAULT_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        if(localDateTime == null){
            return StringUtils.EMPTY;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT_DATE_TIME));
    }

    public static String formatLocalDate(LocalDate localDate) {
        if(localDate == null){
            return StringUtils.EMPTY;
        }
        return localDate.format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT_DATE));
    }

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT_DATE_TIME));
    }

    public static LocalDateTime parseLocalDateTime(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        long timestamp = Timestamp.valueOf(dateTime).getTime();
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDate parseLocalDate(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        long timestamp = Timestamp.valueOf(dateTime).getTime();
        return LocalDate.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static long differDays(Temporal minLocalDateTime, Temporal maxLocalDateTime) {
        if (minLocalDateTime == null || maxLocalDateTime == null) {
            return -1;
        }
        return Duration.between(minLocalDateTime, maxLocalDateTime).toDays();
    }
}
