package team.se.robotapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Time;

import team.se.util.Img_refresh;
import team.se.util.Info_refresh;
import team.se.util.Map_refresh;
import team.se.util.TransContro;

public class ControActivity extends AppCompatActivity {
    private JoyStickView joyStickView;
    private TransContro transContro;
    private static final int BACK = 0;
    private static final int FORWARD = 1;
    private static final int RIGHT = 2;
    private static final int LEFT = 3;
    private static final int IMG_PORT = 1999;
    private static final int MAP_PORT = 2003;
    private static final int INFO_PORT = 2000;
    private static String HOST;
    private static ImageView imageView;
    private static TextView speedText;
    private static TextView conStateText;
    private static TextView locText;
    private static int Switch = 0;
    private static final int DISPLAYCAM = 0;
    private static final int DISPLAYMAP = 1;
    private static final String RECVLOC = "RECVLOC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contro);

        Intent intent = getIntent();
        final String[] addr = intent.getStringExtra("addr").split("\\|");
        HOST = addr[0];

        imageView = (ImageView)findViewById(R.id.camImage);
        LoadHandler loadHandler = new LoadHandler();
        final Img_refresh img_refresh = new Img_refresh(HOST, IMG_PORT, loadHandler);

        speedText = (TextView)findViewById(R.id.speedText);
        conStateText = (TextView)findViewById(R.id.conStateText);

        final Info_refresh info_refresh = new Info_refresh(HOST, INFO_PORT, loadHandler);
        info_refresh.acceptServer();
        transContro = new TransContro(HOST, Integer.valueOf(addr[1]), loadHandler);
        transContro.checkCon();

        final Map_refresh map_refresh = new Map_refresh(HOST, MAP_PORT, loadHandler);

        Button navButton = (Button)findViewById(R.id.buttonNav);
        Button camButton = (Button)findViewById(R.id.displayCam);
        Button mapButton = (Button)findViewById(R.id.displayMap);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_refresh.stopRecv();
                info_refresh.stopRecv();
                transContro.stopCon();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Socket socket = new Socket(HOST, Integer.valueOf(addr[1]));
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(RECVLOC.getBytes());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                Intent intent = new Intent(ControActivity.this, NavActivity.class);
                intent.putExtra("addr", addr[0] + "|" + addr[1]);
                startActivity(intent);
            }
        });
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch = DISPLAYCAM;
                map_refresh.stopRecv();
                img_refresh.startRecv();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch = DISPLAYMAP;
                img_refresh.stopRecv();
                map_refresh.startRecv();
            }
        });

        joyStickView = (JoyStickView) findViewById(R.id.joyStickView);
        joyStickView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_MOVE){
                    int area = joyStickView.getWithinArea(event.getX(), event.getY());
                    switch (area){
                        case BACK:{
                            transContro.moveBackward();
                            break;
                        }
                        case FORWARD:{
                            transContro.moveForward();
                            break;
                        }
                        case LEFT:{
                            transContro.turnLeft();
                            break;
                        }
                        case RIGHT:{
                            transContro.trunRight();
                            break;
                        }
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    transContro.stopMove();
                }
                return false;
            }
        });
    }

    public class LoadHandler extends Handler{
        @Override
        public  void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    if (Switch == DISPLAYCAM)
                        imageView.setImageBitmap((Bitmap)msg.obj);
                    break;
                case 1:
                    if (Switch == DISPLAYMAP)
                        imageView.setImageBitmap((Bitmap)msg.obj);
                    break;
                case 2:
                    speedText.setText((String)msg.obj);
                    break;
                case 3:
                    conStateText.setText((String)msg.obj);
                    break;
            }
        }
    }
}