package team.se.robotapp;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})

public class ControActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private Context appContext;

    @Before
    public void setUp() {
        appContext = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void onCreate() {
        ControActivity activity = Robolectric.buildActivity(ControActivity.class).create().get();

        //JoyStickView

        String s1 = "Navigation";
        assertEquals(s1,((Button)activity.findViewById(R.id.buttonNav)).getText());
        String s2 = "Camera";
        assertEquals(s2,((Button)activity.findViewById(R.id.displayCam)).getText());
        String s3 = "ScanMap";
        assertEquals(s3,((Button)activity.findViewById(R.id.displayCam)).getText());

        //ImageView

        String s4 = appContext.getString(R.string.speed);
        assertEquals(s4,((TextView)activity.findViewById(R.id.speedText)).getText());

        String s5 = appContext.getString(R.string.State_Con);
        assertEquals(s5,((TextView)activity.findViewById(R.id.conStateText)).getText());

    }
}