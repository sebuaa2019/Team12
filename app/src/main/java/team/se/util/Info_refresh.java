package team.se.util;
import android.util.Log;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;

import team.se.robotapp.ControActivity;
import team.se.robotapp.NavActivity;

public class Info_refresh {
    private static String Host;
    private static int Port;
    private static final int ByteLen = 2048;
    private ControActivity.LoadHandler controHandler;
    private NavActivity.LoadHandler navHandler;
    private boolean EXIT;

    public Info_refresh(String host, int port, ControActivity.LoadHandler loadHandler) {
        Host = host;
        Port = port;
        controHandler = loadHandler;
        navHandler = null;
        EXIT = false;
    }

    public Info_refresh(String host, int port, NavActivity.LoadHandler loadHandler) {
        Host = host;
        Port = port;
        controHandler = null;
        navHandler = loadHandler;
        EXIT = false;
    }

    public void acceptServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket socket = new Socket(Host, Port);
                    InputStream inputStream = socket.getInputStream();

                    byte[] bytes = new byte[ByteLen];
                    int length = 0;
                    while ((length = inputStream.read(bytes,0,20))!=-1 && !EXIT){
                        String msg = new String(bytes);
     //                   System.out.println("length is"+length);
      //                  Log.d("INFO", msg);
                        if (controHandler != null)
                            controHandler.obtainMessage(2,msg.substring(0,13)).sendToTarget();
                        else if (navHandler != null)
                            navHandler.obtainMessage(2,msg.substring(0,13)).sendToTarget();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopRecv(){
        this.EXIT = true;
    }
}
