package test;

import model.Game;
import model.Player;
import model.Property;
import org.junit.Before;
import org.junit.Test;
import view.Color;
import view.GameBoard;
import view.Info;
import view.Box;
import view.Brick;

import java.util.ArrayList;

/**
 * @author Liu Yuyang
 */
public class InfoTest {

    private Game game;
    private Info info;
    private Player player1;
    private Player player2;
    private Player player3;
    private GameBoard gameBoard;
    private Box[][] boxes;

    @Before
    public void setUp() {
        player1 = new Player("Alice", Color.RED);
        player2 = new Player("Bob", Color.BLUE);
        player3 = new Player("Charlie", Color.GREEN);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        boxes = new Box[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                boxes[i][j] = new Box(false,
                        new Brick("BoxName", Color.NULL_BACKGROUND, Color.NULL),
                        new Brick("Value", Color.NULL_BACKGROUND, Color.NULL),
                        new Brick("Players", Color.NULL_BACKGROUND, Color.NULL));
            }
        }
        gameBoard = new GameBoard(boxes);
        game = new Game("testFile.txt", gameBoard, players, new ArrayList<>(), false, 1, 0);
        info = new Info(game);
    }

    @Test
    public void testPrintPlayerInfo_BankruptedPlayer() {
        player1.setBroken(true, gameBoard);
        info.printPlayerInfo(player1);
    }

    @Test
    public void testPrintPlayerInfo_NormalPlayerWithProperties() {
        Property property = new Property(1, "Park Place", 350, 50);
        property.setOwner(player2);
        player2.getProperties().add(property);
        info.printPlayerInfo(player2);
    }

    @Test
    public void testPrintPlayerInfo_PlayerInJail() {
        player3.setInJail(true);
        info.printPlayerInfo(player3);
    }

    @Test
    public void testPrintAllPlayerInfo() {
        info.printAllPlayerInfo();
    }

    @Test
    public void testQueryNextPlayer() {
        info.queryNextPlayer(player1); // Should show player2 as next
        info.queryNextPlayer(player2); // Should show player3 as next
        info.queryNextPlayer(player3); // Should loop back to player1
    }

    @Test
    public void testGameOverInfo_AllPlayersBankrupted() {
        player1.setBroken(true, gameBoard);
        player2.setBroken(true, gameBoard);
        player3.setBroken(true, gameBoard);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Info.gameOverInfo(5, players);
    }

    @Test
    public void testGameOverInfo_SingleWinner() {
        player1.setMoney(1000);
        player2.setBroken(true, gameBoard);
        player3.setMoney(500);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Info.gameOverInfo(10, players);
    }

    @Test
    public void testGameOverInfo_MultiplePlayers() {
        player1.setMoney(1500);
        player2.setMoney(1000);
        player3.setMoney(2000);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Info.gameOverInfo(15, players);
    }
}


