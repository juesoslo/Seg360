package security.server.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LogsUtilities {

    public final static String ZONE = "America/Bogota";

    public static String getToday(){
        ZoneId zone = ZoneId.of(ZONE);
        ZonedDateTime dateTime = ZonedDateTime.now(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String actualDate = dateTime.format(formatter);
        return actualDate;
    }
}
