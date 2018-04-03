package com.example.hasee.sae;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by hasee on 2017/10/18.
 */

public class pressure {
    private SensorManager sensorManager = null;
    private Sensor pressure_sensor = null;
    private SensorDB sensorDB = null;
    public pressure(Context context){
        DatabaseContext databaseContext = new DatabaseContext(context);
        sensorDB = new SensorDB(databaseContext);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        pressure_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(listener,pressure_sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                float[] res = new float[3];
                res[0] = event.values[0];
                sensorDB.insert(res[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
