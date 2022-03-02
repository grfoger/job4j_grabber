package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SqlRuDateTimeParserTest {

    @Test
    public void parseTodayYesterday() {
        DateTimeParser parser = new SqlRuDateTimeParser();
        /** CHANGE DATE BEFORE TEST */
        int day = LocalDateTime.now().getDayOfMonth();
        int month = LocalDateTime.now().getMonthValue();
        int year = LocalDateTime.now().getYear();
        assertEquals(parser.parse("вчера, 22:43"),
                LocalDateTime.of(LocalDateTime.now().minusDays(1).getYear(),
                        LocalDateTime.now().minusDays(1).getMonthValue(),
                        LocalDateTime.now().minusDays(1).getDayOfMonth(),
                        22, 43));
        assertEquals(parser.parse("сегодня, 22:43"),
                LocalDateTime.of(year, month, day, 22, 43));
    }

    @Test
    public void parseJan() {
        DateTimeParser parser = new SqlRuDateTimeParser();
        assertEquals(parser.parse("28 янв 22, 19:35"),
                LocalDateTime.of(2022, 1, 28, 19, 35));
    }

    @Test
    public void parseFeb() {
        DateTimeParser parser = new SqlRuDateTimeParser();
        assertEquals(parser.parse("1 фев 22, 19:35"),
                LocalDateTime.of(2022, 2, 1, 19, 35));
    }

    @Test
    public void parseDec() {
        DateTimeParser parser = new SqlRuDateTimeParser();
        assertEquals(parser.parse("31 дек 22, 19:35"),
                LocalDateTime.of(2022, 12, 31, 19, 35));
    }
}
