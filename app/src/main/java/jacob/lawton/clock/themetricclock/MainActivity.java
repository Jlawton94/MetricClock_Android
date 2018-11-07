package jacob.lawton.clock.themetricclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayCurrentTime();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        displayCurrentTime();
                    }
                });
            }
        },100,100);

    }

    private void displayCurrentTime(){
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

        //make strings
        String hours = "0" + Long.toString(metHours);
        String mins = Long.toString(metMins);
        if(metMins < 10){
            mins = "0" + Long.toString(metMins);
        }
        String seconds = Long.toString(metSeconds);
        if(metSeconds < 10){
            seconds = "0" + Long.toString(metSeconds);
        }

        TextView timer = (TextView) findViewById(R.id.timeDisplay);
        timer.setText(hours + ":" + mins + ":" + seconds);
    }
}
