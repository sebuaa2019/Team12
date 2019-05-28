package team.se.util;


import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import team.se.robotapp.ControActivity.LoadHandler;
import team.se.robotapp.NavMapView;


public class Img_refresh {
    private static String Host;
    private static int Port;
    private static boolean EXIT;
    private LoadHandler loadHandler;
    private static Socket socket;
    private NavMapView navMapView;
    private int width;
    private int height;
    private Bitmap.Config config;

    public Img_refresh(String host, int port, LoadHandler _loadhandler){
        this.Host = host;
        this.Port = port;
        this.EXIT = false;
        this.loadHandler = _loadhandler;
        this.navMapView = null;
        this.config = Bitmap.Config.RGB_565;
        width = 720;
        height = 480;
    }

    public Img_refresh(String host, int port, NavMapView navMapView){
        this.Host = host;
        this.Port = port;
        this.EXIT = false;
        this.navMapView = navMapView;
        this.loadHandler = null;
        this.config = Bitmap.Config.ARGB_8888;
        width = 256;
        height = 256;
    }

    public void accpetServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(Host,Port);

                    InputStream inputStream = socket.getInputStream();

                    Bitmap bitmap = Bitmap.createBitmap(width,height, config);

                    byte[] bytes = new byte[10000000];
                    int length, off=0;

                    double time1;

                    while ((length=inputStream.read(bytes,off,1000))!=-1 && !EXIT){
                        off=off+length;
                        if(off>=width * height * 4){
                            off=off-width * height * 4;
                            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes,0,width * height * 4));
                            if (loadHandler != null)
                                loadHandler.obtainMessage(0,bitmap).sendToTarget();
                            else if (navMapView != null)
                                navMapView.setMap(bitmap);
                        }
                    }
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopRecv(){
        EXIT = true;
    }

    public void startRecv(){
        EXIT = false;
        accpetServer();
    }

    public boolean getExit() {
        return EXIT;
    }
}