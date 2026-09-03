package edu.wgu.d387_sample_code.lang;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DisplayTime {

    public static List<String> displayTimes() {
        return ConvertTimes(20, 0);
    }

    public static List<String> ConvertTimes(int hour, int minute){
        List<String> str_out = new ArrayList<>();

        ZoneId zEastern=ZoneId.of("America/New_York");
        ZoneId zUTC= ZoneId.of("UTC");
        ZoneId zMountain=ZoneId.of("America/Denver");
        ZoneId systemZone=ZoneId.systemDefault();
        LocalTime localtime = LocalTime.of(hour, minute);
        DateTimeFormatter form = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime zoneDateTime = ZonedDateTime.of(LocalDate.now(), localtime, systemZone);
        ZonedDateTime zonedDateTimeEastern = zoneDateTime.withZoneSameInstant(zEastern);
        LocalDateTime localDateTimeEastern = zonedDateTimeEastern.toLocalDateTime();
        str_out.add("ET: " + localDateTimeEastern.format(form));
        ZonedDateTime zonedDateTimeUTC = zoneDateTime.withZoneSameInstant(zUTC);
        LocalDateTime localDateTimeUTC = zonedDateTimeUTC.toLocalDateTime();
        str_out.add("UTC: " + localDateTimeUTC.format(form));
        ZonedDateTime zonedDateTimeMountain = zoneDateTime.withZoneSameInstant(zMountain);
        LocalDateTime localDateTimeMountain = zonedDateTimeMountain.toLocalDateTime();
        str_out.add("MT: " + localDateTimeMountain.format(form));
        return str_out;
    }
}
