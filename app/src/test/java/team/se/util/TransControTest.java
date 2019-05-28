package team.se.util;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TransControTest {

    @Test
    public void moveForward() {
        TransContro transContro = Mockito.mock(TransContro.class);
        assertEquals(transContro.getPre_contro(),"");
        transContro.moveForward();
        Mockito.verify(transContro).transMsg("FORWARD");

        assertEquals(transContro.getPre_contro(),"FORWARD");
        Mockito.verify(transContro).transMsg("FORWARD");
    }

    @Test
    public void moveBackward() {
        TransContro transContro = Mockito.mock(TransContro.class);
        assertEquals(transContro.getPre_contro(),"BACKWARD");
        transContro.moveForward();
        Mockito.verify(transContro).transMsg("BACKWARD");

        transContro.setPre_contro("");
        assertEquals(transContro.getPre_contro(),"BACKWARD");
        transContro.moveForward();
        Mockito.verify(transContro).transMsg("BACKWARD");
        assertEquals(transContro.getPre_contro(),"BACKWARD");
    }

    @Test
    public void turnLeft() {
        TransContro transContro = Mockito.mock(TransContro.class);
        assertEquals(transContro.getPre_contro(),"TURNLEFT");
        transContro.moveForward();
        Mockito.verify(transContro).transMsg("TURNLEFT");

        transContro.setPre_contro("");
        assertEquals(transContro.getPre_contro(),"TURNLEFT");
        transContro.moveForward();
        Mockito.verify(transContro).transMsg("TURNLEFT");
        assertEquals(transContro.getPre_contro(),"TURNLEFT");
    }

    @Test
    public void trunRight() {
        TransContro transContro = Mockito.mock(TransContro.class);
        assertEquals(transContro.getPre_contro(),"TURNRIGHT");
        transContro.moveForward();
        Mockito.verify(transContro).transMsg("TURNRIGHT");

        transContro.setPre_contro("");
        assertEquals(transContro.getPre_contro(),"TURNRIGHT");
        transContro.moveForward();
        Mockito.verify(transContro).transMsg("TURNRIGHT");
        assertEquals(transContro.getPre_contro(),"TURNRIGHT");
    }




    @Test
    public void stopMove() {
        TransContro transContro = mock(TransContro.class);
        transContro.stopMove();
        Mockito.verify(transContro).transMsg("STOPMOVE");
        assertEquals("STOPMOVE",transContro.getPre_contro());
    }

    @Test
    public void startNav() {

    }

    @Test
    public void sendTarget() {
    }

    @Test
    public void checkCon() {

    }
}