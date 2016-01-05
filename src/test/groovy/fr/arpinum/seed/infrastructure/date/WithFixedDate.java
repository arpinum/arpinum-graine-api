package fr.arpinum.seed.infrastructure.date;

import org.junit.rules.*;

import java.time.*;
import java.time.temporal.*;
import java.util.*;

public class WithFixedDate extends ExternalResource {

    public static WithFixedDate of(LocalDateTime date) {
        return new WithFixedDate(date);
    }

    public WithFixedDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    protected void before() throws Throwable {
        Dates.initialize(new Dates() {
            @Override
            protected Date getNow() {
                return Dates.toDate(date);
            }
        });
    }

    @Override
    protected void after() {
        Dates.initialize(new Dates());
    }

    public void twoHoursLater() {
        later(2, ChronoUnit.HOURS);
    }

    public void later(int amount, ChronoUnit unit) {
        final LocalDateTime newDate = Dates.localNow().plus(amount, unit);
        Dates.initialize(new Dates() {
            @Override
            protected Date getNow() {
                return toDate(newDate);
            }
        });
    }

    public void setNow(LocalDateTime time) {
        Dates.initialize(new Dates() {
            @Override
            protected Date getNow() {
                return toDate(time);
            }
        });
    }
    private final LocalDateTime date;
}
