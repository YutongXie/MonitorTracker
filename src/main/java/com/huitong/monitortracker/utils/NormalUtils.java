package com.huitong.monitortracker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class NormalUtils {

    public static String formatDateToNormal(Date date) {
        Date inputDate = Optional.ofNullable(date).orElse(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(inputDate);
    }

    public static String formatNumberToString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            Integer integer = (Integer) obj;
            return String.valueOf(integer.intValue());
        }

        if (obj instanceof Long) {
            Long longObj = (Long) obj;
            return String.valueOf(longObj.longValue());
        }

        if (obj instanceof Double) {
            Double doubleObj = (Double) obj;
            return String.valueOf(doubleObj.doubleValue());
        } else {
            return null;
        }
    }
}