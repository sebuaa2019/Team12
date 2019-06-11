package team.se.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import team.se.robotapp.ControActivity;
import team.se.robotapp.NavMapView;

public class Map_refresh {
    private static String Host;
    private static int port;
    private static NavMapView navMapView;
    private static ControActivity.LoadHandler loadHandler;
    private static boolean EXIT;
    private static Socket socket;

    public  Map_refresh(String _Host, int _port, NavMapView _navMapView){
        Host = _Host;
        port = _port;
        navMapView = _navMapView;
        loadHandler = null;
        EXIT = false;
    }

    public Map_refresh(String _Host, int _port, ControActivity.LoadHandler handler){
        Host = _Host;
        port = _port;
        loadHandler = handler;
        navMapView = null;
        EXIT = false;
    }

    public void accpetServer(){
        final int width=256,height=256;
        final Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{


                    socket = new Socket(Host,port);

                    InputStream inputStream = socket.getInputStream();

                    byte[] bytes = new byte[8000000];
                    byte[] bytes2 = new byte[8000000];
                    int length, off=0;

                    double time1;

                    while ((length=inputStream.read(bytes,off,1000))!=-1 && !EXIT){
                        off=off+length;
                        if(off>=width * height){
                //            System.out.println("time : "+System.currentTimeMillis());
                            for(int i=0;i<width * height;i++){
                                    if(bytes[i]==-1){
                                        bytes2[i*4]=(byte)0x80;
                                        bytes2[i*4+1]=(byte)0x80;
                                        bytes2[i*4+2]=(byte)0x80;
                                        bytes2[i*4+3]= (byte) 0xff;
                                    }
                                    else if(bytes[i]==0){
                                        bytes2[i*4]=(byte) 255;
                                        bytes2[i*4+1]=(byte) 255;
                                        bytes2[i*4+2]=(byte) 255;
                                        bytes2[i*4+3]= (byte) 0xff;
                                    }
                                    else{
                                        bytes2[i*4]=0;
                                        bytes2[i*4+1]=0;
                                        bytes2[i*4+2]=0;
                                        bytes2[i*4+3]= (byte) 0xff;
                                    }
                            }

                            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes2,0,width * height *4));
                            if (navMapView != null)
                                navMapView.setMap(bitmap);
                            else if (loadHandler != null)
                                loadHandler.obtainMessage(1, bitmap).sendToTarget();
                            off -= width*height;
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
