package com.newsagregator.dateParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DateParser {
    public static LocalDate parse(String date) {
        if (date.equals(" Сегодня") || date.equals("сегодня")) return LocalDate.now();
        if (date.equals(" Вчера") || date.equals("вчера")) return LocalDate.now().minusDays(1);
        else {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("d MMMM yyyy")
                    .toFormatter(new Locale("ru"));
            return LocalDate.parse(date.substring(1), formatter);
        }
    }

    public static LocalDate parseDate(String date) {
        if (date.equals(" Сегодня") || date.equals("сегодня")) return LocalDate.now();
        if (date.equals(" Вчера") || date.equals("вчера")) return LocalDate.now().minusDays(1);
        else {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("d MMMM yyyy")
                    .toFormatter(new Locale("ru"));
            return LocalDate.parse(date, formatter);
        }
    }
}
