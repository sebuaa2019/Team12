package team.se.robotapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        TextView textViewIp = (TextView)findViewById(R.id.textViewIp);
        TextView textViewPort = (TextView)findViewById(R.id.textViewPort);
        final EditText editTextIp = (EditText)findViewById(R.id.editTextIp);
        final EditText editTextPort = (EditText)findViewById(R.id.editTextPort);
        Button buttonConn = (Button)findViewById(R.id.buttonConn);

        buttonConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final String host = editTextIp.getText().toString();
                            final int port = Integer.valueOf(editTextPort.getText().toString());
                            Log.d("ConThread", host + "|" + port);
                            Socket socket = new Socket(host, port);
                            Intent intent = new Intent(ConnectActivity.this, ControActivity.class);
                            intent.putExtra("addr", host + "|" + port);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
