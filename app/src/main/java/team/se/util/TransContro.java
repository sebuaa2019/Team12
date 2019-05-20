package team.se.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import team.se.robotapp.NavActivity;
import team.se.robotapp.NavMapView;
import team.se.robotapp.R;
import team.se.robotapp.ControActivity;

public class TransContro {
    private String HOST;
    private int PORT;
    private String pre_contro;
    private static final String FORWARD = "FORWARD";
    private static final String BACKWARD = "BACKWARD";
    private static final String TURNLEFT = "TURNLEFT";
    private static final String TURNRIGHT = "TURNRIGHT";
    private static final String STOPMOVE = "STOPMOVE";
    private static final String STARTNAV = "STARTNAV";
    private static final String TRANSMSG = "MSG";
    private static final long INTERVAL = 100000;
    private ControActivity.LoadHandler controHandler;
    private NavActivity.LoadHandler navHandler;
    private static boolean EXIT;

    public TransContro(String HOST, int PORT, ControActivity.LoadHandler handler){
        this.HOST = HOST;
        this.PORT = PORT;
        pre_contro = "";
        this.controHandler = handler;
        this.navHandler = null;
        EXIT = false;
    }

    public TransContro(String HOST, int PORT, NavActivity.LoadHandler handler){
        this.HOST = HOST;
        this.PORT = PORT;
        pre_contro = "";
        this.controHandler = null;
        this.navHandler = handler;
        EXIT = false;
    }

    public TransContro(String HOST, int PORT){
        this.HOST = HOST;
        this.PORT = PORT;
        EXIT = false;
    }

    public void moveForward(){
        if (!pre_contro.equals(FORWARD)) {
            transMsg(FORWARD);
            pre_contro = FORWARD;
        }
    }

    public void moveBackward(){
        if (!pre_contro.equals(BACKWARD)) {
            transMsg(BACKWARD);
            pre_contro = BACKWARD;
        }
    }

    public void turnLeft(){
        if (!pre_contro.equals(TURNLEFT)) {
            transMsg(TURNLEFT);
            pre_contro = TURNLEFT;
        }
    }

    public void trunRight(){
        if (!pre_contro.equals(TURNRIGHT)) {
            transMsg(TURNRIGHT);
            pre_contro = TURNRIGHT;
        }
    }

    public void stopMove(){
        transMsg(STOPMOVE);
        pre_contro = STOPMOVE;
    }

    public void startNav(){
        transMsg(STARTNAV);
    }

    public void sendTarget(float pos_x, float pos_y){
        transMsg("(" + String.valueOf(pos_x) + "," + String.valueOf(pos_y) + ",1.0)");
    }

    private void transMsg(final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try(Socket socket = new Socket(HOST, PORT)){
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())
                    );
                    writer.write(message);
                    writer.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void checkCon(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!EXIT){
                    try{
                        Socket socket = new Socket(HOST, PORT);
                        //OutputStream outputStream = socket.getOutputStream();
                        //outputStream.write(TRANSMSG.getBytes());
                        //conStateText.setText(R.string.State_Con);
                        if (controHandler != null)
                            controHandler.obtainMessage(3,"State : Connected").sendToTarget();
                        else if (navHandler != null)
                            navHandler.obtainMessage(3,"State : Connected").sendToTarget();
                        socket.close();
                    }catch (Exception e){
                        //conStateText.setText(R.string.State_Out);
                        if (controHandler != null)
                            controHandler.obtainMessage(3,"State : Offline").sendToTarget();
                        else if (navHandler != null)
                            navHandler.obtainMessage(3,"State : Offline").sendToTarget();
                        e.printStackTrace();
                    }
                    try{
                        Thread.sleep(INTERVAL);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopCon(){
        EXIT = true;
    }
}
