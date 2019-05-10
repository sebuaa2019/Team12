package team.se.util;
import android.util.Log;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;

import team.se.robotapp.ControActivity;

public class Info_refresh {
    private static String Host;
    private static int Port;
    private static final int ByteLen = 2048;

    public Info_refresh(String host, int port) {
        Host = host;
        Port = port;
    }

    public void acceptServer(final TextView speedText,final ControActivity.LoadHandler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket socket = new Socket(Host, Port);
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);

                    byte[] bytes = new byte[ByteLen];
                    int length = 0;
                    while ((length = inputStream.read(bytes,0,20))!=-1){
                        String msg = new String(bytes);
     //                   System.out.println("length is"+length);
      //                  Log.d("INFO", msg);
                        handler.obtainMessage(2,msg.substring(0,13)).sendToTarget();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
