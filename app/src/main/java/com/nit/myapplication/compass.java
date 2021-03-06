package com.nit.myapplication;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class compass extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private ImageView image;
    private TextView compassAngle;
    private float currentDegree = 0f;
    private float bearingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        Intent in = getIntent();
        image = (ImageView)findViewById(R.id.imageViewCompass);
        compassAngle = (TextView)findViewById(R.id.angle);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);
        bearingValue = 317f;
        float acw = 0f, cw = 0f;
        //angle from destination to source (not the opposite)
        // if clockwise angle is obtained, the user has to turn anticlockwise to make the deviation zero
        if(degree <= bearingValue)
        {
            acw = bearingValue - degree;
            cw = (360f - bearingValue) + degree;
        }
        else
        {
            acw = bearingValue + (360 - degree);
            cw = degree - bearingValue;
        }
        if(cw < acw)
        {
            compassAngle.setText("Heading: " + Float.toString(degree) + " degrees, move " + cw + "deg anti - clockwise");
        }
        else
        {
            compassAngle.setText("Heading: " + Float.toString(degree) + " degrees, move " + acw + "deg clockwise");
        }
        Log.e("anglevalue", Float.toString(degree));
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                                currentDegree,
                                -degree,
                                Animation.RELATIVE_TO_SELF,0.5f,
                                Animation.RELATIVE_TO_SELF,0.5f);

        // how long the animation will take place
        ra.setDuration(210);
        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
