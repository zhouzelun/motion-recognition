package com.example.hasee.sae;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by hasee on 2017/10/18.
 */

public class gyroscope {
    private SensorManager sensorManager = null;
    private Sensor gyroscope_sensor = null;
    private SensorDB sensorDB = null;
    public gyroscope(Context context){
        DatabaseContext databaseContext = new DatabaseContext(context);
        sensorDB = new SensorDB(databaseContext);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscope_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(listener,gyroscope_sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                float[] res = new float[3];
                res[0] = event.values[0];
                res[1] = event.values[1];
                res[2] = event.values[2];
                sensorDB.insert(res[0], res[1], res[2], SensorDB.TPYE_GYROSCOPE);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
