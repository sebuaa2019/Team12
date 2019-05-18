package team.se.robotapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.Socket;

import team.se.util.Map_refresh;
import team.se.util.TransContro;

public class NavActivity extends AppCompatActivity {

    private static String HOST;
    private static int LOC_REF_PORT = 2001;
    private static int MAP_PORT = 2003;
    private static final int MAP_PIXEL_SIZE = 992;
    private static final int MAP_REAL_SIZE = 50;
    private static final int MAP_TRANS = 25;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Intent intent = getIntent();
        String addr[] = intent.getStringExtra("addr").split("\\|");
        HOST = addr[0];
        final TransContro transContro = new TransContro(HOST, Integer.valueOf(addr[1]));

        final NavMapView navMapView = (NavMapView)findViewById(R.id.navMap);
        Button startNav = (Button)findViewById(R.id.startNav);

        // accept map
        Map_refresh map_refresh = new Map_refresh(HOST,MAP_PORT,navMapView);
        map_refresh.accpetServer();

        /////
        navMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float pos_x = event.getX();
                    float pos_y = event.getY();
                    float scale = navMapView.getScale();
                    navMapView.addTarPos(pos_x, pos_y);
                    pos_x = pos_x * scale;
                    pos_y = pos_y * scale;
                    pos_x = pos_x / MAP_PIXEL_SIZE * MAP_REAL_SIZE - MAP_TRANS;
                    pos_y = MAP_TRANS - pos_y / MAP_PIXEL_SIZE * MAP_REAL_SIZE;
                    Log.d("NavMapViewClick", String.valueOf(pos_x) + "|" + String.valueOf(pos_y));
                    transContro.sendTarget(pos_x, pos_y);
                }
                return true;
            }
        });

        startNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transContro.startNav();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(HOST, LOC_REF_PORT);
                    InputStream inputStream = socket.getInputStream();

                    byte[] bytes = new byte[1024];
                    int length = 0;
                    while ((length = inputStream.read(bytes,0,20))!=-1){
                        String[] loc = new String(bytes).split(",");
                        float pos_x = Float.valueOf(loc[0]);
                        float pos_y = Float.valueOf(loc[1]);
                        float scale = navMapView.getScale();
                        pos_x = (MAP_TRANS + pos_x ) * MAP_PIXEL_SIZE / MAP_REAL_SIZE / scale;
                        pos_y = (MAP_TRANS - pos_y) * MAP_PIXEL_SIZE / MAP_REAL_SIZE / scale;
                        navMapView.setRoboPos(pos_x, pos_y);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
