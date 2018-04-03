package com.example.hasee.sae;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hasee on 2017/10/15.
 */

public class All_sensor {
    private SensorManager sensorManager = null;
    private Sensor gravity_sensor = null;
    private Sensor magnetic_sensor = null;
    private Sensor accelerometer_sensor = null;
    private Sensor gyroscope_sensor = null;
    private Sensor pressure_sensor = null;
    private SensorDB sensorDB = null;
    private static final double MAX_AC_X=78.47236,MAX_AC_Y=46.409206,MAX_AC_Z=59.80716;
    private static final double MAX_GR_X=9.8066,MAX_GR_Y=9.8066,MAX_GR_Z=9.8066;
    private static final double MAX_GY_X=13.8544235,MAX_GY_Y=12.7976265,MAX_GY_Z=17.315586;
    private static final double MAX_MA_X=53.1875,MAX_MA_Y=48.5625,MAX_MA_Z=54.0625;
    private static final double MAX_PR=1013.24805;
    private static final double MIN_AC_X=-78.24251,MIN_AC_Y=-67.7176,MIN_AC_Z=-66.29066;
    private static final double MIN_GR_X=-9.8064995,MIN_GR_Y=-9.8066,MIN_GR_Z=-9.7836;
    private static final double MIN_GY_X=-15.998561,MIN_GY_Y=-12.340699,MIN_GY_Z=-17.536718;
    private static final double MIN_MA_X=-53.0625,MIN_MA_Y=-49.875,MIN_MA_Z=-55.625;
    private static final double MIN_PR=997.6333;
    private int count1=0;
    private LinkedBlockingQueue<Float[]> ac = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Float[]> gr = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Float[]> gy = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Float[]> ma = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Float[]> pr = new LinkedBlockingQueue<>();
    int count=0;
    public All_sensor(Context context){
        DatabaseContext databaseContext = new DatabaseContext(context);
        sensorDB = new SensorDB(databaseContext);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravity_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroscope_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetic_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        pressure_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        sensorManager.registerListener(listener,accelerometer_sensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,gravity_sensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,gyroscope_sensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,magnetic_sensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,pressure_sensor,SensorManager.SENSOR_DELAY_GAME);
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true) {
                    if(!ac.isEmpty()) {
                        Float[] tmp = ac.poll();
                        //tmp[0]=(float)Normalize(MAX_AC_X,MIN_AC_X,tmp[0]);
                        //tmp[1]=(float)Normalize(MAX_AC_X,MIN_AC_X,tmp[1]);
                        //tmp[2]=(float)Normalize(MAX_AC_X,MIN_AC_X,tmp[2]);
                        sensorDB.insert(tmp[0],tmp[1],tmp[2], SensorDB.TPYE_ACCELEROMETER);
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true) {
                    if(!gr.isEmpty()) {
                        Float[] tmp = gr.poll();
                        //tmp[0]=(float)Normalize(MAX_GR_X,MIN_GR_X,tmp[0]);
                        //tmp[1]=(float)Normalize(MAX_GR_X,MIN_GR_X,tmp[1]);
                        //tmp[2]=(float)Normalize(MAX_GR_X,MIN_GR_X,tmp[2]);
                        sensorDB.insert(Normalize( MAX_AC_X,MIN_AC_X,tmp[0]),tmp[1],tmp[2],SensorDB.TPYE_GRAVITY);
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true) {
                    if(!gy.isEmpty()) {
                        Float[] tmp = gy.poll();
                        //tmp[0]=(float)Normalize(MAX_GY_X,MIN_GY_X,tmp[0]);
                        //tmp[1]=(float)Normalize(MAX_GY_X,MIN_GY_X,tmp[1]);
                        //tmp[2]=(float)Normalize(MAX_GY_X,MIN_GY_X,tmp[2]);
                        sensorDB.insert(tmp[0],tmp[1],tmp[2], SensorDB.TPYE_GYROSCOPE);
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true) {
                    if(!ma.isEmpty()) {
                        Float[] tmp = ma.poll();
                        //tmp[0]=(float)Normalize(MAX_MA_X,MIN_MA_X,tmp[0]);
                        //tmp[1]=(float)Normalize(MAX_MA_X,MIN_MA_X,tmp[1]);
                        //tmp[2]=(float)Normalize(MAX_MA_X,MIN_MA_X,tmp[2]);
                        if(count1++%2!=0) {
                            sensorDB.insert(tmp[0], tmp[1], tmp[2], SensorDB.TPYE_MAGNETIC);
                        }
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true) {
                    if(!pr.isEmpty()) {
                        //sensorDB.insert(Normalize(MAX_PR,MIN_PR,pr.poll()[0]));
                        sensorDB.insert(pr.poll()[0]);
                    }
                }
            }
        }.start();
    }



    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(final SensorEvent event) {
                        Float[] tmp = {event.values[0], event.values[1], event.values[2]};
                        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                            ac.offer(tmp);
                        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                            gr.offer(tmp);
                        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                            gy.offer(tmp);
                        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                            ma.offer(tmp);
                        } else {
                            pr.offer(tmp);
                        }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };



    public void Destory(){
        sensorManager.unregisterListener(listener);
    }
    public double Normalize(double max,double min,float origin){
        return (origin-min)/(max-min);
    }

}
