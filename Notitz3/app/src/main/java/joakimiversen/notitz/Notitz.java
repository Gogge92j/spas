package joakimiversen.notitz;

import java.sql.Time;

public class Notitz {
    int id;
    boolean activated;
    String title;
    String text;
    int hour;
    int minute;
    boolean repeat;
    boolean monday;
    boolean tuesday;
    boolean wednesday;
    boolean thursday;
    boolean friday;
    boolean saturday;
    boolean sunday;

    Notitz() {

    }

    Notitz(int _id, String _title) {
        id = _id;
        title = _title;
        hour = new Time(System.currentTimeMillis()).getHours();
        minute = new Time(System.currentTimeMillis()).getMinutes() + 5;
        if (minute > 59) {
            minute = minute % 60;
            hour++;
            if (hour > 23) {
                hour = hour % 24;
            }
        }
    }
}
