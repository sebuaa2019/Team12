package team.se.robotapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.Socket;

import team.se.util.Img_refresh;
import team.se.util.Info_refresh;
import team.se.util.Map_refresh;
import team.se.util.TransContro;

public class NavActivity extends AppCompatActivity {

    private static String HOST;
    private static final int INFO_PORT = 2000;
    private static int LOC_REF_PORT = 2001;
    private static int MAP_PORT = 2004;
    private static final int MAP_PIXEL_SIZE = 992;
    private static final int MAP_REAL_SIZE = 50;
    private static final int MAP_TRANS = 25;
    private static TextView stateText;
    private static TextView velText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Intent intent = getIntent();
        String addr[] = intent.getStringExtra("addr").split("\\|");
        HOST = addr[0];

        final NavMapView navMapView = (NavMapView)findViewById(R.id.navMap);
        Button startNav = (Button)findViewById(R.id.startNav);
        velText = findViewById(R.id.velText);
        stateText = findViewById(R.id.stateText);
        LoadHandler loadHandler = new LoadHandler();
        final TransContro transContro = new TransContro(HOST, Integer.valueOf(addr[1]), loadHandler);
        transContro.checkCon();
        Info_refresh info_refresh = new Info_refresh(HOST, INFO_PORT, loadHandler);
        info_refresh.acceptServer();
        // accept map
        Img_refresh img_refresh = new Img_refresh(HOST, MAP_PORT, navMapView);
        img_refresh.accpetServer();

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
                        pos_x = (MAP_TRANS + pos_x) * (MAP_PIXEL_SIZE / MAP_REAL_SIZE) / scale;
                        pos_y = (MAP_TRANS - pos_y) * (MAP_PIXEL_SIZE / MAP_REAL_SIZE) / scale;
                        navMapView.setRoboPos(pos_x, pos_y);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class LoadHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2 : {
                    velText.setText((String)msg.obj);
                    break;
                }
                case 3 : {
                    stateText.setText((String)msg.obj);
                    break;
                }
            }
        }
    }
}
