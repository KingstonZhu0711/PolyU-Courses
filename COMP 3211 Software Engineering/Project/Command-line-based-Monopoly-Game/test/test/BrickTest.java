package test;

import org.junit.Test;
import view.Brick;
import view.Color;

import static org.junit.Assert.*;

/**
 * @author Liu Yuyang
 */
public class BrickTest {


    @Test
    public void testConstructorWithDefaultContent() {
        Brick brick = new Brick("TestContent", Color.RED_BACKGROUND, Color.RED);
        assertEquals("    TestContent    ", brick.getContent());
        assertEquals("TestContent", brick.getPureContent());
        assertEquals(Color.RED_BACKGROUND, brick.getBackgroundColor());
        assertEquals(Color.RED, brick.getFontColor());
    }

    @Test
    public void testConstructorWithCustomContent() {
        Brick brick = new Brick("CustomContent", "PureContent", Color.GREEN_BACKGROUND, Color.GREEN);
        assertEquals("CustomContent", brick.getContent());
        assertEquals("PureContent", brick.getPureContent());
        assertEquals(Color.GREEN_BACKGROUND, brick.getBackgroundColor());
        assertEquals(Color.GREEN, brick.getFontColor());
    }

    @Test
    public void testSetContentAsPure() {
        Brick brick = new Brick("TestContent", Color.RED_BACKGROUND, Color.RED);
        brick.setContentAsPure("NewContent");
        assertEquals("NewContent", brick.getContent());
    }

    @Test
    public void testSetBackgroundColor() {
        Brick brick = new Brick("TestContent", Color.RED_BACKGROUND, Color.RED);
        brick.setBackgroundColor(Color.BLUE_BACKGROUND);
        assertEquals(Color.BLUE_BACKGROUND, brick.getBackgroundColor());
    }

    @Test
    public void testCenterString() {
        String centered = Brick.centerString("Center", Brick.LENGTH_BRICK);
        assertEquals("      Center       ", centered);

        centered = Brick.centerString("", Brick.LENGTH_BRICK);
        assertEquals("                   ", centered);

        centered = Brick.centerString("VeryLongContentThatExceedsLength", Brick.LENGTH_BRICK);
        assertNotEquals("VeryLongContentThatExceedsLength", centered);
    }

    @Test
    public void testToStringWithEmptyValues() {
        Brick brick = new Brick("", "", "", "");
        String expected = ">,>,>,>";
        assertEquals(expected, brick.toString());
    }

    @Test
    public void testToStringWithNonEmptyValues() {
        Brick brick = new Brick("TestContent", "PureContent", Color.RED_BACKGROUND, Color.RED);
        String expected = "TestContent,PureContent," + Color.RED_BACKGROUND + "," + Color.RED;
        assertEquals(expected, brick.toString());
    }
}
