package zelora.api.helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ProjectHelper {

    public static Date getTodaysDate(){
        LocalDate today = LocalDate.now();
        return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date calculateExpirationDate() {
        LocalDate today = LocalDate.now();
        LocalDate expirationLocalDate = today.plusMonths(6);
        Date expirationDate = Date.from(expirationLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return expirationDate;
    }

}
