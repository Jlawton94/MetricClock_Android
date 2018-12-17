package jacob.lawton.clock.themetricclock;

import java.util.Calendar;

public class metricTime {

    public Long[] getMetricTime(){
        //Time in regular Millis
        long timeMillis = System.currentTimeMillis();


        //display metric time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        int mSecond = calendar.get(Calendar.SECOND);

        //build metric time
        long metDaySecs = mHour*3600 + mMinute*60 + mSecond;
        long metSeconds = metDaySecs*100000/86400;
        long metHours = metSeconds/10000;
        metSeconds = metSeconds - 10000 * metHours;
        long metMins = metSeconds/100;
        metSeconds = metSeconds - 100 * metMins;

        return(new Long[]{metHours,metMins,metSeconds});
    }
}
