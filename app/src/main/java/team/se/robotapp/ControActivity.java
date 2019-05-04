package team.se.robotapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.net.Socket;

import team.se.util.TransContro;

public class ControActivity extends AppCompatActivity {
    private JoyStickView joyStickView;
    private TransContro transContro;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int FORWARD = 2;
    private static final int BACK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contro);

        Intent intent = getIntent();
        String[] addr = intent.getStringExtra("addr").split("\\|");
        transContro = new TransContro(addr[0], Integer.valueOf(addr[1]));


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

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
}