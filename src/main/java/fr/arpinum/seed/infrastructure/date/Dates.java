package fr.arpinum.seed.infrastructure.date;

import java.time.*;
import java.util.Date;

public class Dates {

    public static void initialize(Dates instance) {
        INSTANCE = instance;
    }

    public static LocalDateTime localNow() {
        return toLocalDateTime(now());
    }

    public static Date now() {
        return INSTANCE.getNow();
    }

    protected Date getNow() {
        return new Date();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public static Instant instantNow() {
        return INSTANCE.getNow().toInstant();
    }

    public static Date toDate(LocalDate date) {
        return toDate(date.atStartOfDay());
    }

    public static Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneOffset.UTC).toInstant());
    }

    Dates() {

    }

    private static Dates INSTANCE = new Dates();
}
