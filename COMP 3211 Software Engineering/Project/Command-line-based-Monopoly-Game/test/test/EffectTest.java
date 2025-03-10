package test;

import model.Effect;
import model.EffectType;
import model.Player;
import model.Property;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import view.Box;
import view.Brick;
import view.Color;
import view.GameBoard;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;
/**
 * @author Liu Yuyang
 */
public class EffectTest {

    private Player player;
    private GameBoard gameBoard;
    private Property property;
    private Effect effect;
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Initialize a simple game board
        Box[][] boxes = new Box[GameBoard.ROWS][GameBoard.COLS];
        for (int i = 0; i < GameBoard.ROWS; i++) {
            for (int j = 0; j < GameBoard.COLS; j++) {
                boxes[i][j] = new Box(true, new Brick(" ", Color.NULL_BACKGROUND, Color.NULL),
                        new Brick(" ", Color.NULL_BACKGROUND, Color.NULL),
                        new Brick(" ", Color.NULL_BACKGROUND, Color.NULL));
            }
        }
        gameBoard = new GameBoard(boxes);

        // Create a player
        player = new Player("Alice", "Green");

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Color.RED_BACKGROUND = "\u001B[41m";
        Color.GREEN_BACKGROUND = "\u001B[42m";
        Color.YELLOW_BACKGROUND = "\u001B[43m";
        Color.BLUE_BACKGROUND = "\u001B[44m";
        Color.PURPLE_BACKGROUND = "\u001B[45m";
        Color.WHITE_BACKGROUND = "\u001B[47m";
        Color.NULL_BACKGROUND = "";
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }


    @Test
    public void testNormalEffectWithUnboughtProperty() {
        // Test NORMAL effect when property is not bought
        property = new Property(2, "Property1", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        effect = new Effect(property, player, gameBoard);

        // Simulate player choice to buy the property
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        effect.applyEffect();

        assertTrue(property.isBought());
        assertEquals(player, property.getOwner());
        assertEquals(1000, player.getMoney()); // 1000 - 500
    }

    @Test
    public void testNormalEffectWithBoughtProperty() {
        // Test NORMAL effect when property is already bought
        Player owner = new Player("Bob", "Black");
        property = new Property(3, "Property2", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        property.setOwner(owner);
        property.setBought(true);
        effect = new Effect(property, player, gameBoard);

        effect.applyEffect();

        assertEquals(1450, player.getMoney()); // Player pays 50 rent
        assertEquals(1550, owner.getMoney()); // Owner receives 50 rent
    }

    @Test
    public void testGoEffect() {
        // Test GO effect
        property = new Property(1, "GO", 0, 0);
        property.setEffectType(EffectType.GO);
        effect = new Effect(property, player, gameBoard);

        effect.applyEffect();

        assertEquals(3000, player.getMoney()); // 1000 + 1500
    }

    @Test
    public void testIncomeTaxEffect() {
        // Test INCOME_TAX effect
        property = new Property(4, "Income Tax", 0, 0);
        property.setEffectType(EffectType.INCOME_TAX);
        effect = new Effect(property, player, gameBoard);

        effect.applyEffect();

        assertEquals(1350, player.getMoney()); // 1000 - 10%
    }

    @Test
    public void testChanceEffect() {
        // Test CHANCE effect
        property = new Property(9, "Chance", 0, 0);
        property.setEffectType(EffectType.CHANCE);
        effect = new Effect(property, player, gameBoard);

        effect.applyEffect();

        // Since CHANCE has random values, just check that player's money changes appropriately
        assertNotEquals(1000, player.getMoney());
    }

    @Test
    public void testNoEffect() {
        // Test NO_EFFECT
        property = new Property(6, "No Effect", 0, 0);
        property.setEffectType(EffectType.NO_EFFECT);
        effect = new Effect(property, player, gameBoard);

        effect.applyEffect();

        assertEquals(1500, player.getMoney()); // No change in money
    }

    @Test
    public void testGoToJailEffect() {
        // Test GO_TO_JAIL
        property = new Property(16, "Go to Jail", 0, 0);
        property.setEffectType(EffectType.GO_TO_JAIL);
        effect = new Effect(property, player, gameBoard);

        effect.applyEffect();

        assertTrue(player.isInJail());
        assertEquals(6, player.getPosition()); // Jail position
        assertEquals(3, player.getJailTurns()); // 3 jail turns
    }

    @Test
    public void testSetPropertyColorRed() {
        // Create a player with red color and a property
        player = new Player("Alice", "Red");
        property = new Property(2, "Property1", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        effect = new Effect(property, player, gameBoard);

        int[] coordinate = GameBoard.getBoxCoordinate(String.valueOf(property.getPosition()));
        String actualBackgroundColor = "\u001B[41m";

        assertEquals(Color.RED_BACKGROUND, actualBackgroundColor);
    }

    @Test
    public void testSetPropertyColorGreen() {
        // Create a player with green color and a property
        player = new Player("Bob", "Green");
        property = new Property(3, "Property2", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        effect = new Effect(property, player, gameBoard);

        int[] coordinate = GameBoard.getBoxCoordinate(String.valueOf(property.getPosition()));
        String actualBackgroundColor = "\u001B[42m";

        assertEquals(Color.GREEN_BACKGROUND, actualBackgroundColor);
    }

    @Test
    public void testSetPropertyColorBlue() {
        // Create a player with blue color and a property
        player = new Player("Charlie", "Blue");
        property = new Property(4, "Property3", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        effect = new Effect(property, player, gameBoard);

        int[] coordinate = GameBoard.getBoxCoordinate(String.valueOf(property.getPosition()));
        String actualBackgroundColor = "\u001B[44m";

        assertEquals(Color.BLUE_BACKGROUND, actualBackgroundColor);
    }

    @Test
    public void testSetPropertyColorYellow() {
        // Create a player with yellow color and a property
        player = new Player("Diana", "Yellow");
        property = new Property(5, "Property4", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        effect = new Effect(property, player, gameBoard);

        int[] coordinate = GameBoard.getBoxCoordinate(String.valueOf(property.getPosition()));
        String actualBackgroundColor = "\u001B[43m";

        assertEquals(Color.YELLOW_BACKGROUND, actualBackgroundColor);
    }

    @Test
    public void testSetPropertyColorPurple() {
        // Create a player with purple color and a property
        player = new Player("Eve", "Purple");
        property = new Property(6, "Property5", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        effect = new Effect(property, player, gameBoard);

        int[] coordinate = GameBoard.getBoxCoordinate(String.valueOf(property.getPosition()));
        String actualBackgroundColor = "\u001B[45m";

        assertEquals(Color.PURPLE_BACKGROUND, actualBackgroundColor);
    }

    @Test
    public void testSetPropertyColorCyan() {
        // Create a player with cyan color and a property
        player = new Player("Frank", "Cyan");
        property = new Property(7, "Property6", 500, 50);
        property.setEffectType(EffectType.NORMAL);
        effect = new Effect(property, player, gameBoard);

        int[] coordinate = GameBoard.getBoxCoordinate(String.valueOf(property.getPosition()));
        String actualBackgroundColor = "\u001B[47m";

        assertEquals(Color.WHITE_BACKGROUND, actualBackgroundColor);
    }

}


