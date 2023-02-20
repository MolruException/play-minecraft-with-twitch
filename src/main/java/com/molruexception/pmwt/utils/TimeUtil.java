package com.molruexception.pmwt.utils;

import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static String format(@NotNull ZonedDateTime time) {
        return time.format(getDefaultFormat());
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(getDefaultZone());
    }

    public static ZoneId getDefaultZone() {
        return ZoneId.of("Asia/Seoul");
    }

    public static DateTimeFormatter getDefaultFormat() {
        return DateTimeFormatter.ISO_ZONED_DATE_TIME;
    }

}
