package team.se.robotapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class ConnectActivityTest {


    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private Context appContext;

    @Before
    public void setUp() {
        appContext = RuntimeEnvironment.application.getApplicationContext();
    }


    @Test
    public void onCreate() {
        ConnectActivity activity = Robolectric.buildActivity(ConnectActivity.class).create().get();

        String textip = appContext.getString(R.string.anno_ip);
        assertEquals(textip, ((TextView) activity.findViewById(R.id.textViewIp)).getText());

        String textport = appContext.getString(R.string.anno_port);
        assertEquals(textport, ((TextView) activity.findViewById(R.id.textViewPort)).getText());

        String ip = appContext.getString(R.string.context_ip);
        assertEquals(ip, ((EditText) activity.findViewById(R.id.textViewPort)).getText());

        String port = appContext.getString(R.string.context_port);
        assertEquals(port, ((EditText) activity.findViewById(R.id.textViewPort)).getText());

        String textcon = appContext.getString(R.string.anno_Con);
        assertEquals(textcon, ((Button) activity.findViewById(R.id.buttonConn)).getText());
    }

    @Test
    public void button_click_faild() {
        ConnectActivity activity = Robolectric.setupActivity(ConnectActivity.class);
        EditText ip = (EditText) activity.findViewById(R.id.textViewIp);
        EditText port = (EditText)activity.findViewById(R.id.textViewPort);
        ip.setText("000.000.000.00");
        port.setText("0000");
        activity.findViewById(R.id.buttonConn).performClick();
        String textcon = appContext.getString(R.string.isCon);
        assertEquals(textcon, ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void button_click_success() {
        ConnectActivity activity = Robolectric.setupActivity(ConnectActivity.class);
        activity.findViewById(R.id.buttonConn).performClick();
        String host = ((EditText) activity.findViewById(R.id.textViewPort)).getText().toString();
        int port = Integer.valueOf(((TextView) activity.findViewById(R.id.textViewPort)).getText().toString());
        Intent expectedIntent = new Intent(activity, ControActivity.class);
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent, actualIntent);
        assertEquals(host + "|" + port,actualIntent.getStringExtra("addr"));
    }

}

