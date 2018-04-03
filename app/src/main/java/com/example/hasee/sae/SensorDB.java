package com.example.hasee.sae;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasee on 2017/10/15.
 */

public class SensorDB extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "Sensor.db";
    private final static int DATABASE_VERSION = 3;
    private final static String ACCELEROMETER_TABLE = "accelerometer_table";
    private final static String GRAVITY_TABLE = "gravity_table";
    private final static String GYROSCOPE_TABLE = "gyroscope_table";
    private final static String MAGNETIC_TABLE = "magnetic_table";
    private final static String PRESSURE_TABLE = "pressure_table";
    private final static String NUM = "num";
    public final static String ACCELEROMETER = "accelerometer";
    public final static String GRAVITY = "gravity";
    public final static String GYROSCOPE = "gyroscope";
    public final static String MAGNETIC = "magnetic";
    public final static String PRESSURE = "pressure";
    public final static int TPYE_ACCELEROMETER = 0;
    public final static int TPYE_GRAVITY = 1;
    public final static int TPYE_GYROSCOPE = 2;
    public final static int TPYE_MAGNETIC = 3;
    public final static int TPYE_PRESSURE = 4;
    public SensorDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS " + ACCELEROMETER_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + GRAVITY_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + GYROSCOPE_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + MAGNETIC_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + PRESSURE_TABLE;
        db.execSQL(sql);
        sql = "CREATE TABLE " + ACCELEROMETER_TABLE+"("
                + NUM +" INTEGER PRIMARY KEY ,"
                + ACCELEROMETER+"X" + " REAL, "
                + ACCELEROMETER+"Y" + " REAL, "
                + ACCELEROMETER+"Z" + " REAL); ";
        db.execSQL(sql);
        sql = "CREATE TABLE " + GRAVITY_TABLE+"("
                + NUM +" INTEGER PRIMARY KEY,"
                + GRAVITY+"X" + " REAL, "
                + GRAVITY+"Y" + " REAL, "
                + GRAVITY+"Z" + " REAL); ";
        db.execSQL(sql);
        sql = "CREATE TABLE " + GYROSCOPE_TABLE+"("
                + NUM +" INTEGER PRIMARY KEY,"
                + GYROSCOPE+"X" + " REAL, "
                + GYROSCOPE+"Y" + " REAL, "
                + GYROSCOPE+"Z" + " REAL);";
        db.execSQL(sql);
        sql = "CREATE TABLE " + MAGNETIC_TABLE+"("
                + NUM +" INTEGER PRIMARY KEY,"
                + MAGNETIC+"X" + " REAL, "
                + MAGNETIC+"Y" + " REAL, "
                + MAGNETIC+"Z" + " REAL);";
        db.execSQL(sql);
        sql = "CREATE TABLE " + PRESSURE_TABLE+"("
                + NUM +" INTEGER PRIMARY KEY,"
                + PRESSURE+ " REAL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + ACCELEROMETER_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + GRAVITY_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + GYROSCOPE_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + MAGNETIC_TABLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + PRESSURE_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(double x,double y,double z,int mode){
        SQLiteDatabase db = this.getWritableDatabase();//获取一个可读写数据库
        ContentValues cv = new ContentValues();
        //cv.put(NUM,num);
        switch (mode){
            case TPYE_ACCELEROMETER:
                cv.put(ACCELEROMETER+"X", x);
                cv.put(ACCELEROMETER+"Y", y);
                cv.put(ACCELEROMETER+"Z", z);
                db.insert(ACCELEROMETER_TABLE, null, cv);break;
            case TPYE_GRAVITY:
                cv.put(GRAVITY+"X", x);
                cv.put(GRAVITY+"Y", y);
                cv.put(GRAVITY+"Z", z);
                db.insert(GRAVITY_TABLE, null, cv);break;
            case TPYE_GYROSCOPE:
                cv.put(GYROSCOPE+"X", x);
                cv.put(GYROSCOPE+"Y", y);
                cv.put(GYROSCOPE+"Z", z);
                db.insert(GYROSCOPE_TABLE, null, cv);break;
            case TPYE_MAGNETIC:
                cv.put(MAGNETIC+"X", x);
                cv.put(MAGNETIC+"Y", y);
                cv.put(MAGNETIC+"Z", z);
                db.insert(MAGNETIC_TABLE, null, cv);break;
            default:break;
        }
    }

    public void insert(double x){
        SQLiteDatabase db = this.getWritableDatabase();//获取一个可读写数据库
        ContentValues cv = new ContentValues();
        //cv.put(NUM, num);
        cv.put(PRESSURE, x);
        db.insert(PRESSURE_TABLE, null, cv);
    }

    public Cursor select(int limit){
        SQLiteDatabase db = this.getReadableDatabase();//获取一个可读写数据库
        String sql = "SELECT accelerometerX,accelerometerY,accelerometerZ,  " +
                "gravityX,gravityY,gravityZ, " +
                "gyroscopeX,gyroscopeY,gyroscopeZ, " +
                "magneticX,magneticY,magneticZ, " +
                "pressure FROM "+
                ACCELEROMETER_TABLE+","
                +GRAVITY_TABLE+","
                +GYROSCOPE_TABLE+","
                +MAGNETIC_TABLE+","
                +PRESSURE_TABLE+
                " where accelerometer_table.num=gravity_table.num and " +
                "gravity_table.num = gyroscope_table.num and " +
                "gyroscope_table.num = magnetic_table.num and " +
                "magnetic_table.num = pressure_table.num  order by accelerometer_table.num desc limit "+limit;
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    private void delete(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from "+table_name+"  where num in (select num from "+table_name+" limit 500)";
        db.execSQL(sql);

    }

    private void delete_all500() {
        delete(ACCELEROMETER_TABLE);
        delete(GRAVITY_TABLE);
        delete(GYROSCOPE_TABLE);
        delete(MAGNETIC_TABLE);
        delete(PRESSURE_TABLE);
    }

}
