package be.katho.sensor_ball;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class BallActivity extends Activity implements SensorEventListener {
    /**
     * Called when the activity is first created.
     */
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    private ImageView circle;
    private Integer currentMarginX, currentMarginY;
    private Point deviceSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        circle = (ImageView) findViewById(R.id.circle);
        deviceSize = getDeviceSize();
        currentMarginX = (int) (deviceSize.x / 2 - convertDipToPix(10));
        currentMarginY = (int) (deviceSize.y / 2 - convertDipToPix(10));
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            Integer toX = currentMarginX + ((int) (sensorEvent.values[0] * 10));
            Integer toY = currentMarginY - ((int) (sensorEvent.values[1] * 10));

            Log.v("Katho", "X: " + toX + " Y: " + toY + " delta x: " + (int) (sensorEvent.values[0] * 10) + " deltaY: " + (int) (sensorEvent.values[1] * 10));

            if (toX <= 0) {
                toX = 0;
            }

            if (toY <= 0) {
                toY = 0;
            }

            if (toX >= (deviceSize.x - convertDipToPix(20))) {
                toX = (int) (deviceSize.x - convertDipToPix(20));
            }

            if (toY >= (deviceSize.y - convertDipToPix(20))) {
                toX = (int) (deviceSize.y - convertDipToPix(20));
            }
            Animation animation = new TranslateAnimation(currentMarginX, toX, currentMarginY, toY);

            animation.setDuration(1000);
            animation.setFillAfter(true);
            circle.startAnimation(animation);
            currentMarginX = toX;
            currentMarginY = toY;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private Point getDeviceSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private float convertDipToPix(int dipValue) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
}
