package security.server.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LogsUtilities {

    public final static String ZONE = "America/Bogota";
    public final static String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss a";

    public static String getToday(){
        ZoneId zone = ZoneId.of(ZONE);
        ZonedDateTime dateTime = ZonedDateTime.now(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String actualDate = dateTime.format(formatter);
        return actualDate;
    }
}
