package team.se.util;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import team.se.robotapp.R;

public class TransContro {
    private String HOST;
    private int PORT;
    private String pre_contro;
    private static final String FORWARD = "FORWARD";
    private static final String BACKWARD = "BACKWARD";
    private static final String TURNLEFT = "TURNLEFT";
    private static final String TURNRIGHT = "TURNRIGHT";
    private static final String STOPMOVE = "STOPMOVE";
    private static final long INTERVAL = 100000;

    public TransContro(String HOST, int PORT){
        this.HOST = HOST;
        this.PORT = PORT;
        pre_contro = "";
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

    public void sendTarget(float target_X, float target_Y){
        String target = String.valueOf(target_X) + "|" + String.valueOf(target_Y);
        transMsg(target);
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

    public void checkCon(final TextView conStateText){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        Socket socket = new Socket(HOST, PORT);
                        conStateText.setText(R.string.State_Con);
                    }catch (Exception e){
                        conStateText.setText(R.string.State_Out);
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
}
