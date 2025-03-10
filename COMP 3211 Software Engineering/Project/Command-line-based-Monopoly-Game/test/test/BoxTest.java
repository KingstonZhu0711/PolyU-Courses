package test;

import model.Player;
import org.junit.Before;
import org.junit.Test;
import view.Brick;
import view.Color;
import view.Box;

import static org.junit.Assert.*;

/**
 * @author Liu Yuyang
 */
public class BoxTest {

    private Box propertyBox;
    private Box nonPropertyBox;
    private Box customBox;
    private Brick nameBrick;
    private Brick valueBrick;
    private Brick playerBrick;
    private Brick topBrick;

    @Before
    public void setUp() {
        nameBrick = new Brick("Property", Color.RED_BACKGROUND, Color.RED);
        valueBrick = new Brick("Value", Color.GREEN_BACKGROUND, Color.GREEN);
        playerBrick = new Brick("Players", Color.BLUE_BACKGROUND, Color.BLUE);
        topBrick = new Brick("TopBrick", Color.YELLOW_BACKGROUND, Color.YELLOW);

        propertyBox = new Box(true, nameBrick, valueBrick, playerBrick);
        nonPropertyBox = new Box(false, nameBrick, valueBrick, playerBrick);
        customBox = new Box(topBrick, nameBrick, valueBrick, playerBrick);
    }

    @Test
    public void testConstructorWithPropertyBlock() {
        assertEquals("  *-------------*  ", propertyBox.getTopBrick().getContent());
        assertEquals("  *-------------*  ", propertyBox.getBottomBrick().getContent());
    }

    @Test
    public void testConstructorWithoutPropertyBlock() {
        assertEquals("                   ", nonPropertyBox.getTopBrick().getContent());
        assertEquals("                   ", nonPropertyBox.getBottomBrick().getContent());
    }

    @Test
    public void testConstructorWithCustomBricks() {
        assertEquals(topBrick, customBox.getTopBrick());
        assertEquals(nameBrick, customBox.getNameBrick());
        assertEquals(valueBrick, customBox.getValueBrick());
        assertEquals(playerBrick, customBox.getPlayerBrick());
        assertEquals("  *-------------*  ", customBox.getBottomBrick().getContent());
    }

    @Test
    public void testChangePlayerBrickPureContentNoPlayers() {
        propertyBox.changePlayerBrickPureContent();
        assertEquals("                   ", propertyBox.getPlayerBrick().getContent());
    }

    @Test
    public void testChangePlayerBrickPureContentOnePlayer() {
        Player player = new Player("Alice", Color.RED);
        propertyBox.getPlayers().add(player);

        propertyBox.changePlayerBrickPureContent();
        assertNotEquals("        [A]        ", propertyBox.getPlayerBrick().getContent());
    }

    @Test
    public void testChangePlayerBrickPureContentMultiplePlayers() {
        Player player1 = new Player("Alice", Color.RED);
        Player player2 = new Player("Bob", Color.BLUE);
        Player player3 = new Player("Charlie", Color.YELLOW);
        propertyBox.getPlayers().add(player1);
        propertyBox.getPlayers().add(player2);
        propertyBox.getPlayers().add(player3);

        propertyBox.changePlayerBrickPureContent();
        assertNotEquals("     [A][B][C]     ", propertyBox.getPlayerBrick().getContent());
    }

    @Test
    public void testChangePlayerBrickPureContentWithJailedPlayers() {
        Player player1 = new Player("Alice", Color.RED);
        Player player2 = new Player("Bob", Color.BLUE);
        player2.setInJail(true);
        propertyBox.getPlayers().add(player1);
        propertyBox.getPlayers().add(player2);

        propertyBox.changePlayerBrickPureContent();
        assertNotEquals("      [A]>B<       ", propertyBox.getPlayerBrick().getContent());
    }

    @Test
    public void testChangePlayerBrickPureContentMaxPlayers() {
        for (int i = 1; i <= 6; i++) {
            propertyBox.getPlayers().add(new Player("Player" + i, Color.RED));
        }

        propertyBox.changePlayerBrickPureContent();
        assertTrue(propertyBox.getPlayerBrick().getContent().trim().length() > 0);
    }

    @Test
    public void testGetters() {
        assertEquals(nameBrick, propertyBox.getNameBrick());
        assertEquals(valueBrick, propertyBox.getValueBrick());
        assertEquals(playerBrick, propertyBox.getPlayerBrick());
        assertEquals("  *-------------*  ", propertyBox.getBottomBrick().getContent());
        assertNotNull(propertyBox.getPlayers());
    }

    @Test
    public void testToString() {
        String expected = propertyBox.getTopBrick().toString() + "<" +
                propertyBox.getNameBrick().toString() + "<" +
                propertyBox.getValueBrick().toString() + "<" +
                propertyBox.getPlayerBrick().toString();
        assertEquals(expected, propertyBox.toString());
    }

    @Test
    public void testChangePlayerBrickPureContentEmptyBrick() {
        nonPropertyBox.changePlayerBrickPureContent();
        assertEquals("                   ", nonPropertyBox.getPlayerBrick().getContent());
    }
}

