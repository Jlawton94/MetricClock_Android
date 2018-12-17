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
        },10,10);

    }

    private void displayCurrentTime(){

        metricTime currentTime = new metricTime();
        Long[] metTime = currentTime.getMetricTime();

        long metHours = metTime[0];
        long metMins = metTime[1];
        long metSeconds = metTime[2];

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
