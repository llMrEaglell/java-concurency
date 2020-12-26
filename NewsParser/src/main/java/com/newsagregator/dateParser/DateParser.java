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
}
