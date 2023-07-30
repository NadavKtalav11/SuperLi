package BusinessLayer.Tools;

import java.util.List;
import java.util.ArrayList;
import java.time.DayOfWeek;

public class DayConverter {
    public static String convertDays(List<Integer> days) {
        List<String> dayNames = new ArrayList<>();
        for (int day : days) {
            dayNames.add(DayOfWeek.of(day).toString());
        }
        return String.join(", ", dayNames);
    }
}
