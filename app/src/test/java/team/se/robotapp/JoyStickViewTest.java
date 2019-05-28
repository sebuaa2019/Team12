package team.se.robotapp;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.EditText;

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
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})



public class JoyStickViewTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private Context appContext;
    private ControActivity activity;
    private JoyStickView joyStickView;

    @Before
    public void setUp() {
        appContext = RuntimeEnvironment.application.getApplicationContext();
        activity = Robolectric.buildActivity(ControActivity.class).create().get();
        joyStickView = (JoyStickView) activity.findViewById(R.id.joyStickView);
    }

    @Test
    public void onDraw() {

    }

    @Test
    public void onTouchEvent() {
        MotionEvent motionEvent = mock(MotionEvent.class);
        motionEvent.setAction(MotionEvent.ACTION_UP);
        joyStickView.onTouchEvent(motionEvent);
        assertEquals(joyStickView.getDot_x(),joyStickView.getInit_pos().x,0);
        assertEquals(joyStickView.getDot_y(),joyStickView.getInit_pos().y,0);

        motionEvent.setAction(MotionEvent.ACTION_MOVE);
        joyStickView.onTouchEvent(motionEvent);

        motionEvent.setAction(MotionEvent.ACTION_DOWN);
        joyStickView.onTouchEvent(motionEvent);
        /*
        float distance = (float) Math.sqrt(
                    Math.pow(init_pos.x - event.getX(), 2) + Math.pow(init_pos.y - event.getY(), 2)
            );
            if (distance < panel_r){
                dot_x = event.getX();
                dot_y = event.getY();
            }
            else{
                // dot will stick to the edge of panel
                dot_x = (panel_r / distance) * (event.getX() - init_pos.x) + init_pos.x;
                dot_y = (panel_r / distance) * (event.getY() - init_pos.y) + init_pos.y;
            }
         */
    }

    @Test
    public void getWithinArea() {

        float part = (float)0.5;
        float part2 = (float)Math.sqrt(2.0) / 2;

        float touch_x1 = -part;
        float touch_y1 = part2;
        assertEquals(0,joyStickView.getWithinArea(touch_x1,touch_y1));

        float touch_x2 = part;
        float touch_y2 = part2;
        assertEquals(0,joyStickView.getWithinArea(touch_x2,touch_y2));

        float touch_x3 = -part;
        float touch_y3 = -part2;
        assertEquals(1,joyStickView.getWithinArea(touch_x3,touch_y3));

        float touch_x4 = part;
        float touch_y4 = -part2;
        assertEquals(1,joyStickView.getWithinArea(touch_x4,touch_y4));

        float touch_x5 = part2;
        float touch_y5 = part;
        assertEquals(2,joyStickView.getWithinArea(touch_x5,touch_y5));

        float touch_x6 = part2;
        float touch_y6 = -part;
        assertEquals(2,joyStickView.getWithinArea(touch_x6,touch_y6));

        float touch_x7 = -part2;
        float touch_y7 = part;
        assertEquals(3,joyStickView.getWithinArea(touch_x7,touch_y7));

        float touch_x8 = -part2;
        float touch_y8 = -part;
        assertEquals(3,joyStickView.getWithinArea(touch_x8,touch_y8));

        float touch_x = part2;
        float touch_y = part2;
        assertEquals(-1,joyStickView.getWithinArea(touch_x,touch_y));

        touch_x = -part2;
        touch_y = -part2;
        assertEquals(-1,joyStickView.getWithinArea(touch_x,touch_y));

        touch_x = -part2;
        touch_y = part2;
        assertEquals(-1,joyStickView.getWithinArea(touch_x,touch_y));

        touch_x = part2;
        touch_y = -part2;
        assertEquals(-1,joyStickView.getWithinArea(touch_x,touch_y));

    }
}