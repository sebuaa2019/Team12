package team.se.robotapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

    @After
    public void tearDown() {
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
        activity.findViewById(R.id.buttonConn).performClick();
        String textcon = appContext.getString(R.string.isCon);
        assertEquals(textcon, ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void button_click_success() {
        ConnectActivity activity = Robolectric.setupActivity(ConnectActivity.class);
        activity.findViewById(R.id.buttonConn).performClick();
        Intent expectedIntent = new Intent(activity, ControActivity.class);
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent, actualIntent);
    }
}

