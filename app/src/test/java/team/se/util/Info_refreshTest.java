package team.se.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class Info_refreshTest {

    @Test
    public void acceptServer() {
    }

    @Test
    public void stopRecv() {
        Info_refresh info_refresh = mock(Info_refresh.class);
        info_refresh.stopRecv();
        assertEquals(true,info_refresh.getExit());
    }
}