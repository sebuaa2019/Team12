package team.se.util;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Info_refresh {
    private static String Host;
    private static int Port;
    private static final int ByteLen = 2048;

    public Info_refresh(String host, int port) {
        Host = host;
        Port = port;
    }

    public void acceptServer(final TextView speedText) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket socket = new Socket(Host, Port);
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[ByteLen];
                    int length = 0;
                    while ((length = inputStream.read(bytes))!=-1){
                        speedText.setText(bytes.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
