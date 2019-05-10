package team.se.robotapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import team.se.util.TransContro;

public class NavActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Intent intent = getIntent();
        String addr[] = intent.getStringExtra("addr").split("\\|");
        final TransContro transContro = new TransContro(addr[0], Integer.valueOf(addr[1]));

        ImageView mapImage = (ImageView) findViewById(R.id.navImage);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.map);
        mapImage.setImageBitmap(bitmap);
        //缩放比例
        final float scale = 2/getResources().getDisplayMetrics().density;

        mapImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    float pos_x = event.getX();
                    float pos_y = event.getY();
                    transContro.sendTarget(pos_x*scale, pos_y*scale);
                }
                return true;
            }
        });
    }
}
