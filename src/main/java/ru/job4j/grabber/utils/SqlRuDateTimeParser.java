package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    LocalDateTime now = LocalDateTime.now();

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
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int month = 0;
        int day = 0;
        int year = 0;
        if (date.length != 1) {
            day = Integer.parseInt(date[0]);
            year = 2000 + Integer.parseInt(date[2]);
            month = MONTHS.get(date[1]);
        } else {
            if ("сегодня".equals(date[0])) {
                day = now.getDayOfMonth();
                year = now.getYear();
                month = now.getMonthValue();
            } else if ("вчера".equals(date[0])) {
                day = now.minusDays(1).getDayOfMonth();
                year = now.minusDays(1).getYear();
                month = now.minusDays(1).getMonthValue();
            }
        }
        return LocalDateTime.of(year, month, day, hours, minutes);
    }

}