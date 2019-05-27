package team.se.robotapp;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertEquals;

public class NavActivityTest {

    @Test
    public void onCreate() {
        NavActivity activity = Robolectric.buildActivity(NavActivity.class).create().get();

        //NavMapView

        String s1 = activity.getString(R.string.startNav);
        assertEquals(s1, ((Button) activity.findViewById(R.id.startNav)).getText());

        String s2 = activity.getString(R.string.speed);
        assertEquals(s2, ((TextView) activity.findViewById(R.id.velText)).getText());

        String s3 = activity.getString(R.string.State_Con);
        assertEquals(s3, ((TextView) activity.findViewById(R.id.stateText)).getText());

    }

}