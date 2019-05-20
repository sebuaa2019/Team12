package team.se.robotapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.nio.channels.FileLock;
import java.util.ArrayList;

public class NavMapView extends AppCompatImageView {

    private Bitmap map;
    private Bitmap roboBitmap;
    private Bitmap tarBitmap;
    private static float roboPos_X;
    private static float roboPos_Y;
    private static float roboPos_r;
    private static float tarPos_r;
    private static ArrayList<Float> tarPos_X;
    private static ArrayList<Float> tarPos_Y;
    private static float viewWidth;
    private static float viewHeight;
    private static float scale;
    private static boolean isMapReady = false;

    public NavMapView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        //this.map = BitmapFactory.decodeResource(getResources(), R.mipmap.map).copy(Bitmap.Config.RGB_565, true);
        this.map = null;
        this.roboBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.robot_pos);
        this.tarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.target_pos);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (map != null) {
                    viewWidth = getWidth();
                    viewHeight = getHeight();
                    Log.d("ViewSize", String.valueOf(viewWidth) + " " + String.valueOf(viewHeight));
                    roboPos_X = viewWidth / 2;
                    roboPos_Y = viewHeight / 2;
                    Log.d("MapSize", String.valueOf(map.getWidth()) + " " + String.valueOf(map.getHeight()));
                    Log.d("Density", String.valueOf(getResources().getDisplayMetrics().density));
                    scale = (float) map.getHeight() / viewHeight;
                    Log.d("Scale", String.valueOf(scale));
                    getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    NavMapView.this.postInvalidate();
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        roboPos_r = 10.0f;
        tarPos_r = 10.0f;

        tarPos_X = new ArrayList<Float>();
        tarPos_Y = new ArrayList<Float>();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (this.map != null) {
            canvas.drawBitmap(map, null, new Rect(
                    0, 0, (int) viewHeight, (int) viewWidth
            ), null);
            canvas.drawBitmap(roboBitmap, null, new Rect(
                    (int) (roboPos_X - roboPos_r),
                    (int) (roboPos_Y - roboPos_r),
                    (int) (roboPos_X + roboPos_r),
                    (int) (roboPos_Y + roboPos_r)
            ), null);

            for (int i = 0; i < tarPos_X.size(); i++) {
                float tarPos_x = tarPos_X.get(i);
                float tarPos_y = tarPos_Y.get(i);
                canvas.drawBitmap(tarBitmap, null, new Rect(
                        (int) (tarPos_x - tarPos_r),
                        (int) (tarPos_y - 2 * tarPos_r),
                        (int) (tarPos_x + tarPos_r),
                        (int) (tarPos_y)
                ), null);
            }
            isMapReady = true;
        }
    }

    public void setRoboPos(float roboPos_x, float roboPos_y){
        roboPos_X = roboPos_x;
        roboPos_Y = roboPos_y;
    }

    public void addTarPos(float tarPos_x, float tarPos_y){
        tarPos_X.add(tarPos_x);
        tarPos_Y.add(tarPos_y);
    }

    public void removeTarPos(int tarPos_index){
        tarPos_X.remove(tarPos_index);
        tarPos_Y.remove(tarPos_index);
    }

    public void setMap(Bitmap map){
        this.map = map;
    }

    public float getScale(){
        return scale;
    }

    public boolean mapReady(){
        return isMapReady;
    }
}
