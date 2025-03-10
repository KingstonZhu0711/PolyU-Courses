package test;

import org.junit.Before;
import org.junit.Test;
import view.Box;
import view.GameBoard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Liu Yuyang
 */
public class GameBoardTest {

    private static final String TEST_GAME_BOARD_FILE = "testGameBoard.txt";
    private GameBoard gameBoard;
    private Box[][] boxes;

    @Before
    public void setUp() throws IOException {
        // 创建测试文件
        createTestGameBoardFile();
        boxes = GameBoard.initializeBoxes(TEST_GAME_BOARD_FILE);
        gameBoard = new GameBoard(boxes);

    }

    @Test
    public void testInitializeBoxes() {
        assertNotNull(boxes);
        assertEquals(GameBoard.ROWS, boxes.length);
        assertEquals(GameBoard.COLS, boxes[0].length);

        // 检查一个特殊位置的box
        Box box = boxes[5][5]; // 对应位置1
        assertNotNull(box);
        assertEquals("1 TestProperty", box.getNameBrick().getPureContent());
        assertEquals("P500 R50", box.getValueBrick().getPureContent());
    }

    @Test
    public void testGetBoxCoordinate() {
        int[] coord1 = GameBoard.getBoxCoordinate("1");
        assertArrayEquals(new int[]{5, 5}, coord1);

        int[] coord16 = GameBoard.getBoxCoordinate("16");
        assertArrayEquals(new int[]{0, 5}, coord16);

        int[] coord10 = GameBoard.getBoxCoordinate("10");
        assertArrayEquals(new int[]{1, 0}, coord10);

        int[] invalidCoord = GameBoard.getBoxCoordinate("100");
        assertNull(invalidCoord);
    }

    @Test
    public void testPrintBoard() {
        gameBoard.printBoard();
    }

    @Test
    public void testBoxAttributes() {
        Box testBox = boxes[5][5];
        assertEquals("1 TestProperty", testBox.getNameBrick().getPureContent());
        assertEquals("P500 R50", testBox.getValueBrick().getPureContent());
        assertEquals("                   ", testBox.getPlayerBrick().getPureContent());
        assertEquals("  *-------------*  ", testBox.getTopBrick().getContent());
        assertEquals("  *-------------*  ", testBox.getBottomBrick().getContent());
    }

    private void createTestGameBoardFile() throws IOException {
        File file = new File(TEST_GAME_BOARD_FILE);
        if (file.exists()) {
            file.delete();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("1,TestProperty,500,50\n");
            writer.write("2,TestEmpty,-1,-1\n");
            writer.write("3,AnotherProperty,400,40\n");
            // 添加更多行以填充测试
        }
    }
}
