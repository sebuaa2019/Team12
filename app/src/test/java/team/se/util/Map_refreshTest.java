package team.se.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class Map_refreshTest {

    @Test
    public void accpetServer() {
    }

    @Test
    public void stopRecv() {
        Map_refresh map_refresh = mock(Map_refresh.class);
        map_refresh.startRecv();
        assertEquals(true,map_refresh.getExit());
    }

    @Test
    public void startRecv() {
        Map_refresh map_refresh = mock(Map_refresh.class);
        map_refresh.startRecv();
        assertEquals(false,map_refresh.getExit());
    }
}