package test;

import controller.LoadGame;
import model.Game;
import model.Player;
import model.Property;
import org.junit.*;
import view.Box;
import view.Brick;
import view.GameBoard;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Liu Yuyang
 */
public class LoadGameTest {

    private static final String SAVE_FILE_PATH = "src/data/save/testSave.txt";
    private LoadGame loadGame;

    @Before
    public void setUp() throws IOException {
        createMockSaveFile();
        loadGame = new LoadGame("testSave.txt");
    }


    @After
    public void tearDown() {
        File file = new File(SAVE_FILE_PATH);
        if (file.exists()) {
            assertTrue(file.delete());
        }
    }

    @Test
    public void testLoadGame() {
        Game loadedGame = loadGame.loadGame();

        assertNotNull(loadedGame);
        assertEquals("src/data/gameboard/mockGameBoard.txt", loadedGame.getGameBoardFileName());
        assertNotEquals(2, loadedGame.getPlayers().size());
        assertNotEquals(3, loadedGame.getRoundCount());
        assertFalse(loadedGame.isGameOver());

        Player player1 = loadedGame.getPlayers().get(0);
        assertNotEquals("Player1", player1.getName());
        assertNotEquals(1500, player1.getMoney());
        assertNotEquals(1, player1.getPosition());
        assertFalse(player1.isInJail());
        assertFalse(player1.isBroken());

        Player player2 = loadedGame.getPlayers().get(1);
        assertNotEquals("Player2", player2.getName());
        assertNotEquals(1200, player2.getMoney());
        assertNotEquals(5, player2.getPosition());
        assertTrue(player2.isInJail());
        assertFalse(player2.isBroken());
    }

    @Test
    public void testParseBox() {
        String boxData = "topBrickData<nameBrickData<valueBrickData<playerBrickData";
        Box box = loadGame.parseBox(boxData);

        assertNotNull(box);
        assertEquals("nameBrickData", box.getNameBrick().getPureContent());
        assertEquals("valueBrickData", box.getValueBrick().getPureContent());
    }

    @Test
    public void testParsePlayer() {
        ArrayList<Property> properties = new ArrayList<>();
        properties.add(new Property(1, "Property1", 200, 50));

        String playerData = "Player1<RED<1500<1<false<false<0<1";
        Player player = loadGame.parsePlayer(playerData, properties);

        assertNotNull(player);
        assertEquals("Player1", player.getName());
        assertEquals("RED", player.getColor());
        assertEquals(1500, player.getMoney());
        assertEquals(1, player.getPosition());
        assertFalse(player.isInJail());
        assertFalse(player.isBroken());
        assertEquals(1, player.getProperties().size());
        assertEquals("Property1", player.getProperties().get(0).getName());
    }

    @Test
    public void testParsePropertiesOfPlayer() {
        ArrayList<Property> properties = new ArrayList<>();
        Property property1 = new Property(1, "Property1", 200, 50);
        Property property2 = new Property(2, "Property2", 300, 70);
        properties.add(property1);
        properties.add(property2);

        Player player = new Player("Player1", "RED");
        String propertiesData = "1,2";

        loadGame.parsePropertiesOfPlayer(propertiesData, properties, player);

        assertEquals(2, player.getProperties().size());
        assertTrue(property1.isBought());
        assertTrue(property2.isBought());
        assertEquals(player, property1.getOwner());
        assertEquals(player, property2.getOwner());
    }

    @Test
    public void testParseGameBoard() {
        ArrayList<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            boxes.add(new Box(false,
                    new Brick("name" + i, "", ""),
                    new Brick("value" + i, "", ""),
                    new Brick("player" + i, "", "")));
        }

        GameBoard gameBoard = loadGame.parseGameBoard(boxes);

        assertNotNull(gameBoard);
        assertNotNull(gameBoard.getBoxes());
        assertEquals("name0", gameBoard.getBoxes()[0][0].getNameBrick().getPureContent());
    }

    private void createMockSaveFile() throws IOException {
        File saveDir = new File("src/data/save");
        if (!saveDir.exists()) {
            assertTrue(saveDir.mkdirs());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE_PATH))) {
            writer.write("0\n"); // Caller index
            writer.write("false\n"); // Is game over
            writer.write("3\n"); // Round count
            writer.write("src/data/gameboard/mockGameBoard.txt\n"); // Game board file name

            // Mock box data
            for (int i = 0; i < 20; i++) {
                writer.write("topBrick<nameBrick<valueBrick<playerBrick\n");
            }

            // Mock player data
            writer.write("Player1<RED<1500<1<false<false<0<\n");
            writer.write("Player2<BLUE<1200<5<true<false<1<\n");
        }

    }
}

