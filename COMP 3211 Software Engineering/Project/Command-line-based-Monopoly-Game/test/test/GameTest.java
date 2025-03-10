package test;

import model.Game;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;
import org.junit.Assert;
import org.junit.Test;
import util.InputFlagUtil;
import util.InputUtil;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Liu Yuyang
 */

public class GameTest {

//    static List<Player> playerList = new ArrayList<>();

    @Test
    public void testGameInit() throws Throwable {
        final String fileName = "src/data/test/testGameBoard.txt";
        final AtomicReference<Game> atomicReference = new AtomicReference<>();
//        playerList.add(new Player("test1", Player.PLAYER_COLORS[0]));
//        playerList.add(new Player("test2", Player.PLAYER_COLORS[1]));
        final InputFlagUtil inputFlagUtil = new InputFlagUtil();
        TestRunnable testRunnable = new TestRunnable() {
            @Override
            public void runTest() throws Throwable {
                InputUtil.put(Thread.currentThread(), inputFlagUtil);
                Game game = new Game(fileName);
                game.start();
                atomicReference.set(game);
                InputUtil.exit();
            }
        };
        TestRunnable mainRunnable = new TestRunnable() {
            @Override
            public void runTest() throws Throwable {
                InputUtil.put(Thread.currentThread(), inputFlagUtil);
//                InputUtil.in(playerList.size() + "");
//                for (Player player : playerList) {
//                    InputUtil.in(player.getName());
//                }
                InputUtil.in("3");
                InputUtil.in("r");
                InputUtil.in("r");
                InputUtil.in("r");
                InputUtil.in("1");
                InputUtil.in("1");
                InputUtil.in("2");
                InputUtil.in("2");
                InputUtil.in("3");
                InputUtil.in("3");

                InputUtil.in("4");
                InputUtil.in("1");
                InputUtil.in("1");
                InputUtil.in("5");

                InputUtil.in("6");
                InputUtil.in("6");
                InputUtil.in("test");
                InputUtil.in("test");
                InputUtil.in("7");
                InputUtil.in("1");
                InputUtil.in("1");
                InputUtil.in("3");
                InputUtil.in("3");
                InputUtil.in("8");
                InputUtil.in("1");


                InputUtil.exit();
            }
        };
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(new TestRunnable[]{mainRunnable, testRunnable});
        mttr.runTestRunnables();

        Assert.assertNotNull(atomicReference.get());
        Assert.assertEquals("", fileName, atomicReference.get().getGameBoardFileName());
    }

}
