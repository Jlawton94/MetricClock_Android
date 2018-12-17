package jacob.lawton.clock.themetricclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.Console;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DigitalMetricClockWidget extends AppWidgetProvider {

    /**
     * Custom Intent name that is used by the AlarmManager to tell us to update the clock once per second.
     */
    public static String CLOCK_WIDGET_UPDATE = "jacob.lawton.clock.themetricclock.widget.digitalWidgetUpdate";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
            // Get the widget manager and ids for this widget provider, then call the shared
            // clock update method.
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID: ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);

            }
        }
    }

    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60000, createClockTickIntent(context));
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, DigitalMetricClockWidget.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.digital_clock_widget_layout);
            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);


            // Update The clock label using a shared method
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context,	AppWidgetManager appWidgetManager, int appWidgetId) {

        Log.d("MetricClock", "upadting widget");

        metricTime currentTime = new metricTime();
        Long[] metTime = currentTime.getMetricTime();

        long metHours = metTime[0];
        long metMins = metTime[1];

        //make strings
        String hours = "0" + Long.toString(metHours);
        String mins = Long.toString(metMins);
        if(metMins < 10){
            mins = "0" + Long.toString(metMins);
        }

        RemoteViews updateViews = new RemoteViews(context.getPackageName(),	R.layout.digital_clock_widget_layout);
        updateViews.setTextViewText(R.id.digitalTime, hours + ":" + mins);
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }
}
