package com.newsagregator.dateParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DateParser {
    public static LocalDate parse(String date) {
        if (date.equals(" Сегодня")) return LocalDate.now();
        if (date.equals(" Вчера")) return LocalDate.now().minusDays(1);
        else {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("d MMMM yyyy")
                    .toFormatter(new Locale("ru"));
            return LocalDate.parse(date.substring(1), formatter);
        }
    }

    public static String getMonth(int numberMonth) {
        switch (numberMonth) {
            case 1:
                return "january";
            case 2:
                return "february";
            case 3:
                return "march";
            case 4:
                return "april";
            case 5:
                return "may";
            case 6:
                return "june";
            case 7:
                return "july";
            case 8:
                return "august";
            case 9:
                return "september";
            case 10:
                return "october";
            case 11:
                return "november";
            case 12:
                return "december";
            default:
                return "";
        }
    }
}
