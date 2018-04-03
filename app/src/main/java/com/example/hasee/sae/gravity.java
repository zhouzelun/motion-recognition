package com.example.hasee.sae;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by hasee on 2017/10/18.
 */

public class gravity {
    private SensorManager sensorManager = null;
    private Sensor gravity_sensor = null;
    private SensorDB sensorDB = null;
    public gravity(Context context){
        DatabaseContext databaseContext = new DatabaseContext(context);
        sensorDB = new SensorDB(databaseContext);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gravity_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(listener,gravity_sensor,SensorManager.SENSOR_DELAY_UI);
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                float[] res = new float[3];
                res[0] = event.values[0];
                res[1] = event.values[1];
                res[2] = event.values[2];
                sensorDB.insert(res[0], res[1], res[2], SensorDB.TPYE_GRAVITY);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
