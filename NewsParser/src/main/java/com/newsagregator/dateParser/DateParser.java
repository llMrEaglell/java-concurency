package com.newsagregator.dateParser;

import java.time.LocalDate;

public class DateParser {
    public static LocalDate parse(String date) {
        if (date.equals(" Сегодня")) return LocalDate.now();
        if (date.equals(" Вчера")) return LocalDate.now().minusDays(1);
        else {
            String[] dates = date.split(" ");
            int day = Integer.parseInt(dates[1]);
            int month = getMonth(dates[2]);
            int year = Integer.parseInt(dates[3]);
            return LocalDate.of(year, month, day);
        }
    }

    private static int getMonth(String month) {
        switch (month) {
            case "січня":
            case "январь":
            case "января":
                return 1;
            case "лютого":
            case "февраль":
            case "февраля":
                return 2;
            case "березня":
            case "март":
            case "марта":
                return 3;
            case "квітня":
            case "апрель":
            case "апреля":
                return 4;
            case "травня":
            case "май":
            case "мая":
                return 5;
            case "червня":
            case "июнь":
            case "июня":
                return 6;
            case "липня":
            case "июль":
            case "июля":
                return 7;
            case "серпня":
            case "август":
            case "августа":
                return 8;
            case "вересня":
            case "сентябрь":
            case "сентября":
                return 9;
            case "жовтня":
            case "октябрь":
            case "октября":
                return 10;
            case "листопада":
            case "ноябрь":
            case "ноября":
                return 11;
            case "грудня":
            case "декабря":
                return 12;
            default:
                System.out.println(month);
                return 0;
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
