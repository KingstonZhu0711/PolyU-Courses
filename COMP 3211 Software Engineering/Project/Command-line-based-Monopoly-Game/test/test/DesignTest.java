package test;

import controller.Design;
import controller.Initialization;
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
public class DesignTest {


    @Test
    public void testPrintProperty() throws Throwable {

        final AtomicReference<Design> atomicReference = new AtomicReference<>();
        final InputFlagUtil inputFlagUtil = new InputFlagUtil();
        TestRunnable testRunnable = new TestRunnable() {
            @Override
            public void runTest() throws Throwable {
                InputUtil.put(Thread.currentThread(), inputFlagUtil);
                Design design = new Design(Initialization.startGameOnCustomGameBoardQuery());
                design.startDesign();
                atomicReference.set(design);
                InputUtil.exit();
            }
        };
        TestRunnable mainRunnable = new TestRunnable() {
            @Override
            public void runTest() throws Throwable {
                InputUtil.put(Thread.currentThread(), inputFlagUtil);
                InputUtil.in("2");
                InputUtil.in("3");
                InputUtil.in("4");
                InputUtil.in("2");
                InputUtil.in("1");
                InputUtil.in("test");
                InputUtil.in("1");
                InputUtil.in("1");

                InputUtil.in("f");
                InputUtil.in("1");
                InputUtil.in("2");
                InputUtil.in("1");
                InputUtil.in("1");
                InputUtil.in("3");
                InputUtil.in("2");
                InputUtil.in("8");
                InputUtil.in("1");
            }
        };
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(new TestRunnable[]{mainRunnable, testRunnable});
        mttr.runTestRunnables();

        Assert.assertNotNull(atomicReference.get());

    }
}
