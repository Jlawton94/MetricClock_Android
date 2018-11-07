package jacob.lawton.clock.themetricclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

public class analogMetricClock extends View {
    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int numeralSpacing = 0;
    private int handTruncation, hourHandTruncation = 0;
    private int radius = 0;
    private Paint paint;
    private boolean isInit;
    private int[] numbers = {1,2,3,4,5,6,7,8,9,10};
    private Rect rect = new Rect();

    private Path path;

    public analogMetricClock(Context context) {
        super(context);
    }

    public analogMetricClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public analogMetricClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = numeralSpacing + 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        path = new Path();
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initClock();
        }

        canvas.drawColor(Color.WHITE);
        drawCircle(canvas);
        drawCenter(canvas);
        drawNumeral(canvas);
        drawHands(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }

    private void drawHand(Canvas canvas, double loc, boolean isHour, int colour) {
        paint.setColor(getResources().getColor(colour));
        double angle = Math.PI * loc / 50 - Math.PI / 2;
        int handRadius = isHour ? radius - handTruncation - hourHandTruncation : radius - handTruncation;
        canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * handRadius),
                (float) (height / 2 + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawHands(Canvas canvas) {
        Calendar c = Calendar.getInstance();

        double mHour = c.get(Calendar.HOUR_OF_DAY);
        double mMinute = c.get(Calendar.MINUTE);
        double mSecond = c.get(Calendar.SECOND);

        //build metric time
        double metDaySecs = mHour*3600 + mMinute*60 + mSecond;
        double metSeconds = metDaySecs*100000/86400;
        double metHours = Math.floor(metSeconds/10000);
        metSeconds = Math.floor(metSeconds - 10000 * metHours);
        double metMins = Math.floor(metSeconds/100);
        metSeconds = Math.floor(metSeconds - 100 * metMins);

        drawHand(canvas, (metHours + metMins / 100) * 10f, true,android.R.color.holo_blue_dark);
        drawHand(canvas, metMins, false,android.R.color.holo_blue_dark);
        drawHand(canvas, metSeconds, false,android.R.color.holo_red_dark);
    }

    private void drawNumeral(Canvas canvas) {
        paint.setTextSize(fontSize);

        for (int number : numbers) {
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 5 * (number - 2.5);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, 12, paint);
    }

    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        setPolygon(width / 2, height / 2, radius + padding - 10, 10);
        canvas.drawPath(path, paint);
    }

    public void setPolygon(float x, float y, float radius, int numOfPt){

        double section = 2.0 * Math.PI/numOfPt;

        path.reset();
        path.moveTo(
                (float)(x + radius * Math.cos(0.35)),
                (float)(y + radius * Math.sin(0.35)));

        for(int i=1; i<numOfPt; i++){
            path.lineTo(
                    (float)(x + radius * Math.cos(section * (i+0.5))),
                    (float)(y + radius * Math.sin(section * (i+0.5))));
        }

        path.close();

    }
}
