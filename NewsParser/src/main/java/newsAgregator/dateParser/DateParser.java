package newsAgregator.dateParser;

import java.time.LocalDate;

public class DateParser {
    public static LocalDate parse(String date) {
        String[] dates = date.split(" ");
        int day = Integer.parseInt(dates[1]);
        int month = getMonth(dates[2]);
        int year = Integer.parseInt(dates[3]);
        return LocalDate.of(year, month, day);
    }

    private static int getMonth(String month) {
        switch (month) {
            case "січня":
                return 1;
            case "лютого":
                return 2;
            case "березня":
                return 3;
            case "квітня":
                return 4;
            case "травня":
                return 5;
            case "червня":
                return 6;
            case "липня":
                return 7;
            case "серпня":
                return 8;
            case "вересня":
                return 9;
            case "жовтня":
                return 10;
            case "листопада":
                return 11;
            case "грудня":
                return 12;
            default:
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
