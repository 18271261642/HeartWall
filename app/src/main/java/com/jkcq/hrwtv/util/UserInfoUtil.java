package com.jkcq.hrwtv.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by peng on 2018/5/9.
 */

public class UserInfoUtil {

    public static int parseAge(long time) throws ParseException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            String birthday = dateFormat.format(new Date(time));
            Date date = dateFormat.parse(birthday);
            Calendar calendar = Calendar.getInstance();
            Calendar currentcalendar = Calendar.getInstance();
            calendar.setTime(date);
            return currentcalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }
}
