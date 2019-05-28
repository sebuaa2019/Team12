package team.se.robotapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

public class JoyStickView extends View {
    //joystick panel pos, radius and bitmap
    private float panel_x;
    private float panel_y;
    private float panel_r;
    private Bitmap panel_bitmap;
    //joystick dot pos, radius and bitmap
    private float dot_x;
    private float dot_y;
    private float dot_r;
    private Bitmap dot_bitmap;
    //joystick initial pos
    private PointF init_pos;

    public JoyStickView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        //get bitmap from resource
        panel_bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.joystick_panel);
        dot_bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.joystick_dot);
        //set joystick pos and radius
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                init_pos = new PointF(getWidth()/2, getHeight()/2);
                float scale = (float) panel_bitmap.getWidth() / (panel_bitmap.getWidth() + dot_bitmap.getWidth());
                panel_x = init_pos.x;
                panel_y = init_pos.y;
                panel_r = scale * getWidth() / 2;
                dot_x = init_pos.x;
                dot_y = init_pos.y;
                dot_r = (1.0f - scale) * getWidth() / 2;
                return true;
            }
        });
        //create new thread refreshing joystick
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    JoyStickView.this.postInvalidate();
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(panel_bitmap, null, new Rect(
                (int) (panel_x - panel_r),
                (int) (panel_y - panel_r),
                (int) (panel_x + panel_r),
                (int) (panel_y + panel_r)
        ), null);
        canvas.drawBitmap(dot_bitmap, null, new Rect(
                (int)(dot_x - dot_r),
                (int)(dot_y - dot_r),
                (int)(dot_x + dot_r),
                (int)(dot_y + dot_r)
        ), null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE){
            //get distance between touch pos and init pos
            float distance = (float) Math.sqrt(
                    Math.pow(init_pos.x - event.getX(), 2) + Math.pow(init_pos.y - event.getY(), 2)
            );
            if (distance < panel_r){
                dot_x = event.getX();
                dot_y = event.getY();
            }
            else{
                // dot will stick to the edge of panel
                dot_x = (panel_r / distance) * (event.getX() - init_pos.x) + init_pos.x;
                dot_y = (panel_r / distance) * (event.getY() - init_pos.y) + init_pos.y;
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            dot_x = init_pos.x;
            dot_y = init_pos.y;
        }
        return true;
    }

    public float getDot_x() {
        return dot_x;
    }

    public float getDot_y() {
        return dot_y;
    }

    public PointF getInit_pos() {
        return init_pos;
    }

    public int getWithinArea(float touch_x, float touch_y){
        float dist = (float)Math.sqrt(Math.pow(touch_x - init_pos.x, 2) +
                Math.pow(touch_y - init_pos.y, 2));
        float sin = (touch_y - init_pos.y) / dist;
        float part = (float)Math.sqrt(2.0) / 2;
        if ((sin > part || sin < -part) && touch_y > init_pos.y){
            return 0;
        }
        else if ((sin > part || sin < -part) && touch_y < init_pos.y){
            return 1;
        }
        else if ((sin > -part && sin < part) && touch_x > init_pos.x){
            return 2;
        }
        else if ((sin > -part && sin < part) && touch_x < init_pos.x){
            return 3;
        }
        return -1;
    }
}
