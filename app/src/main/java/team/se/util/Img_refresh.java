package team.se.util;


import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import team.se.robotapp.ControActivity.LoadHandler;



public class Img_refresh {
    private static String Host;
    private static int Port;

    public Img_refresh(String host, int port){
        this.Host = host;
        this.Port = port;
    }

    public void accpetServer(ImageView imageView, final LoadHandler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket socket = new Socket(Host,Port);

                    InputStream inputStream = socket.getInputStream();

                    int width=720,height=480;
                    Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

                    byte[] bytes = new byte[10000000];
                    int length, off=0;

                    double time1;

                    while ((length=inputStream.read(bytes,off,10000))!=-1){
                        off=off+length;
                        if(off>=1382400){
                            off=off-1382400;
                            System.out.println("time : "+System.currentTimeMillis());

                            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes,0,1382400));
                            handler.obtainMessage(0,bitmap).sendToTarget();
                        }
                    }
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}