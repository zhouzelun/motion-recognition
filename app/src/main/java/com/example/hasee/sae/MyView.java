package com.example.hasee.sae;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by zzl on 2015/11/25.
 */
public class MyView extends View {
    private float height;
    private float width;
    private int current_index = 0;
    private int h_gz;
    private int w_gz;
    private boolean flag = false;
    public MyView(Context context) {
        super(context);
    }
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        File sdkRoot = Environment.getExternalStorageDirectory();
        String ping_path = sdkRoot.getAbsolutePath() ;
        MMatrix testx;
        Byte[][] s;
        try {
            testx = new MMatrix(ping_path+"/data/test_x.mat");
        }catch (Exception e){

        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = Bitmap.createBitmap(100,100, null);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        canvas.drawBitmap(bitmap,100,100,null);
    }
}
