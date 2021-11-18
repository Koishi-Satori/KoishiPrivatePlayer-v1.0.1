package top.kkoishi.tools;

import java.util.Calendar;

public class Timer {
    String time;
    Calendar calendar;

    public Timer () {
        time = "";
    }

    public void flush () {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        this.time = "[" + year + "/" + month + "/" + day + "]" + hour + ":" + min + ":" + sec;
    }

    public String getTime () {
        return time;
    }
}
