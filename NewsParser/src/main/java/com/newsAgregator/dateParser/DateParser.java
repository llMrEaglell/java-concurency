package newsAgregator.dateParser;

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
                return 1;
            case "лютого":
            case "февраль":
                return 2;
            case "березня":
            case "март":
                return 3;
            case "квітня":
            case "апрель":
                return 4;
            case "травня":
            case "май":
                return 5;
            case "червня":
            case "июнь":
                return 6;
            case "липня":
            case "июль":
                return 7;
            case "серпня":
            case "август":
                return 8;
            case "вересня":
            case "сентябрь":
                return 9;
            case "жовтня":
            case "октябрь":
                return 10;
            case "листопада":
            case "ноябрь":
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
