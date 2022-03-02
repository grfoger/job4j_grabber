package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    public static final String TODAY = "сегодня";
    public static final String YESTERDAY = "вчера";

    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            Map.entry("янв", 1),
            Map.entry("фев", 2),
            Map.entry("мар", 3),
            Map.entry("апр", 4),
            Map.entry("май", 5),
            Map.entry("июн", 6),
            Map.entry("июл", 7),
            Map.entry("авг", 8),
            Map.entry("сен", 9),
            Map.entry("окт", 10),
            Map.entry("ноя", 11),
            Map.entry("дек", 12)
    );

    @Override
    public LocalDateTime parse(String parse) {

        String[] dateTime = parse.split(", ");
        String[] date = dateTime[0].split(" ");
        String[] time = dateTime[1].split(":");
        LocalTime currentTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        LocalDate currentDate = LocalDate.MIN;
        if (date.length != 1) {
            currentDate = LocalDate.of(
                    2000 + Integer.parseInt(date[2]),
                    MONTHS.get(date[1]),
                    Integer.parseInt(date[0]));
        } else if (TODAY.equals(date[0])) {
            currentDate = LocalDate.now();
        } else if (YESTERDAY.equals(date[0])) {
            currentDate = LocalDate.now().minusDays(1);
        }
        return LocalDateTime.of(currentDate, currentTime);
    }

}