package com.example.hasee.sae;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button start;
    private ProgressBar progressBar;
    private static TextView content,left;
    private LinearLayout res;
    private nn mynn;
    private accelerometer _accelerometer;
    private gravity _gravity;
    private gyroscope _gyroscope ;
    private magnetic _magnetic;
    private pressure _pressure;
    private All_sensor all_sensor;
    private MMatrix test_data;
    int count2=1;
    Cursor cursor;
    SensorDB sensorDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.begin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        content = (TextView) findViewById(R.id.content);
        left = (TextView) findViewById(R.id.left);
        res = (LinearLayout) findViewById(R.id.res_visible);
        DatabaseContext databaseContext = new DatabaseContext(this);
        sensorDB = new SensorDB(databaseContext);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(0);
                handler.sendEmptyMessage(8);
                if(!thread.isAlive()) {
                    thread.start();
                }
            }
        });
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                handler.sendEmptyMessage(3);
                mynn = new nn();
                handler.sendEmptyMessage(6);
                handler.sendEmptyMessage(7);
                all_sensor = new All_sensor(MainActivity.this);
                handler.sendEmptyMessage(6);
                handler.sendEmptyMessage(9);
                //thread_test.start();
            }catch (Exception e){}
        }
    };

    Thread thread_test = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                sleep(10000);
                while(true) {
                    try {
                        sleep(100);
                    }catch (Exception e){}
                    handler.sendEmptyMessage(10);
                }
            }catch (Exception e){}
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:content.setText("");break;
                case 3:content.setText(content.getText()+"\nloading nn...");break;
                case 4:content.setText(content.getText()+"\ntesting...");break;
                case 6:content.setText(content.getText()+"\ndone!");break;
                case 7:content.setText(content.getText()+"\ninit sensor...");break;
                case 8:progressBar.setVisibility(ProgressBar.VISIBLE);start.setVisibility(Button.GONE);break;
                case 9:progressBar.setVisibility(ProgressBar.GONE);res.setVisibility(ProgressBar.VISIBLE);content.setVisibility(TextView.GONE);break;
                case 10:
                    cursor = sensorDB.select(61);
                    List<MMatrix> res = new ArrayList<>();
                    if (cursor.moveToFirst()) {
                        do{
                            double[][] value = new double[1][13];
                            for(int i = 0;i<13;i++){
                                value[0][i] = cursor.getDouble(i);
                            }
                            MMatrix tmp = new MMatrix(value);
                            res.add(tmp);
                        }while (cursor.moveToNext());
                    }
                    test_data = MMatrix.matrixmixByrow(res);//61*13
                    test_data = MMatrix.getMySpecialMM(13,5).matrixProduct(test_data);//13*61
                    MMatrix test_matrix = new MMatrix(1,173,0);
                    int count=0;
                    for(int i = 0;i<13;i++){
                        for(int j = 0 ;j<13;j++){
                            test_matrix.setElement(0,count++,test_data.getElement(i,j));
                        }
                    }
                    //double accelerometer_dis =  Math.sqrt(test_matrix.getElement(0,0))
                    test_matrix.setElement(0,count++,getODis(test_data.getElement(0,0),test_data.getElement(0,1),test_data.getElement(0,2)));
                    test_matrix.setElement(0,count++,getODis(test_data.getElement(0,3),test_data.getElement(0,4),test_data.getElement(0,5)));
                    test_matrix.setElement(0,count++,getODis(test_data.getElement(0,6),test_data.getElement(0,7),test_data.getElement(0,8)));
                    test_matrix.setElement(0,count++,getODis(test_data.getElement(0,9),test_data.getElement(0,10),test_data.getElement(0,11)));
                    double  x = mynn.nntest(test_matrix).getElement(0,0);
                    left.setText(x+1+"          "+count2++);
                    break;
            }
        }
    };
    public static void setContent(String str){
        content.setText(content.getText()+"\n"+str);
    }
    private double getODis(double x,double y,double z){
        return Math.sqrt(x*x+y*y+z*z);
    }
}
