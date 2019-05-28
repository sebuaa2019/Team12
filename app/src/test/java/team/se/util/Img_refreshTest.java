package team.se.util;

import android.view.MotionEvent;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class Img_refreshTest {

    @Test
    public void accpetServer() {
    }

    @Test
    public void stopRecv() {
        Img_refresh img_refresh = mock(Img_refresh.class);
        img_refresh.startRecv();
        assertEquals(true,img_refresh.getExit());
    }

    @Test
    public void startRecv() {
        Img_refresh img_refresh = mock(Img_refresh.class);
        img_refresh.startRecv();
        assertEquals(false,img_refresh.getExit());
    }
}