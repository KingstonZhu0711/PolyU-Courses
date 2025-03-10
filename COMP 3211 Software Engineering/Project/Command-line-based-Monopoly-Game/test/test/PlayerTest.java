package test;

import model.Player;
import model.Property;
import org.junit.After;
import org.junit.Test;
import view.Color;
import view.GameBoard;

import static model.Player.initializePlayers;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author Liu Yuyang
 */
public class PlayerTest {

    @Test
    public void testParameterizedConstructor() {
        ArrayList<Property> testProperties = new ArrayList<>();
        Property testProperty1 = new Property(3, "Park Place", 200, 20);
        Property testProperty2 = new Property(5, "Boardwalk", 300, 30);
        testProperties.add(testProperty1);
        testProperties.add(testProperty2);

        Player paramPlayer = new Player("Player1", Color.BLUE, 1000, 10, true, true, 2, testProperties);

        assertEquals("Player1", paramPlayer.getName());
        assertEquals(Color.BLUE, paramPlayer.getColor());
        assertEquals(1000, paramPlayer.getMoney());
        assertEquals(10, paramPlayer.getPosition());
        assertTrue(paramPlayer.isInJail());
        assertTrue(paramPlayer.isBroken());
        assertEquals(2, paramPlayer.getJailTurns());
        assertEquals(2, paramPlayer.getProperties().size());

        Property retrievedProperty1 = paramPlayer.getProperties().get(0);
        Property retrievedProperty2 = paramPlayer.getProperties().get(1);
        assertEquals(3, retrievedProperty1.getPosition());
        assertEquals("Park Place", retrievedProperty1.getName());
        assertEquals(200, retrievedProperty1.getPrice());
        assertEquals(20, retrievedProperty1.getRent());

        assertEquals(5, retrievedProperty2.getPosition());
        assertEquals("Boardwalk", retrievedProperty2.getName());
        assertEquals(300, retrievedProperty2.getPrice());
        assertEquals(30, retrievedProperty2.getRent());
    }

    @Test
    public void testConstructorAndGetters() {
        Player player = new Player("TestPlayer", "RED");

        assertEquals("TestPlayer", player.getName());
        assertEquals("RED", player.getColor());
        assertEquals(1500, player.getMoney());
        assertEquals(1, player.getPosition());
        assertFalse(player.isInJail());
        assertFalse(player.isBroken());
        assertEquals(0, player.getJailTurns());
        assertNotNull(player.getProperties());
        assertTrue(player.getProperties().isEmpty());
    }

    @Test
    public void testSetters() {
        Player player = new Player("TestPlayer", "RED");

        player.setMoney(2000);
        assertEquals(2000, player.getMoney());

        player.setPosition(10);
        assertEquals(10, player.getPosition());

        player.setInJail(true);
        assertTrue(player.isInJail());

        player.setBroken(true, null); // Null `GameBoard` for simplicity
        assertTrue(player.isBroken());

        player.setJailTurns(3);
        assertEquals(3, player.getJailTurns());
    }

    @Test
    public void testAddProperty() {
        Player player = new Player("TestPlayer", "RED");
        Property property = new Property(1, "TestProperty", 100, 10);

        player.getProperties().add(property);
        assertEquals(1, player.getProperties().size());
        assertEquals(property, player.getProperties().get(0));
    }

    @Test
    public void testToString() {
        Player player = new Player("TestPlayer", "RED");
        Property property = new Property(1, "TestProperty", 100, 10);
        player.getProperties().add(property);

        String expected = "TestPlayer<RED<1500<1<false<false<0<1,";
        assertEquals(expected, player.toString());
    }


    @Test
    public void testGenerateRandomPlayerName() {
        String randomName = Player.generateRandomPlayerName();
        assertNotNull(randomName);
        assertTrue(randomName.length() > 0);
    }

    @Test
    public void testInitializePlayersValidInput() {
        String input = "3\nAlice\nBob\nr\n";
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ArrayList<Player> players = Player.initializePlayers();

            assertEquals(3, players.size());
            assertEquals("Alice", players.get(0).getName());
            assertEquals(Color.RED, players.get(0).getColor());
            assertEquals("Bob", players.get(1).getName());
            assertEquals(Color.GREEN, players.get(1).getColor());
            assertNotNull(players.get(2).getName());
            assertEquals(Color.YELLOW, players.get(2).getColor());
        } finally {
            System.setIn(originalIn);
        }
    }

}

