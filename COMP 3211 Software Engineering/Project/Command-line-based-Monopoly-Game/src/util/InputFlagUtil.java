package util;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to assist in testing the game by providing a way to simulate user input.
 * @author Liu Yuyang
 */
public class InputFlagUtil {

    private  final AtomicBoolean ATOMIC_BOOLEAN = new AtomicBoolean(false);

    private  final Object OBJECT = new Object();

    private  AtomicBoolean exitFlag = new AtomicBoolean(false);

    private  ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("".getBytes());

    public  void set(boolean flag) {
        ATOMIC_BOOLEAN.set(flag);
        synchronized (OBJECT) {
            if (flag) {
                OBJECT.notifyAll();
                if (exitFlag.get()) {
                    return;
                }
            }
        }
    }

    public  Boolean get() {
        return ATOMIC_BOOLEAN.get();
    }

    public  String next() {
        set(true);
        while (ATOMIC_BOOLEAN.get()) {
            synchronized (OBJECT) {
                OBJECT.notifyAll();
            }
        }
        Scanner nameScanner = new Scanner(System.in);
        return nameScanner.next();
    }

    public  Object getObject() {
        return OBJECT;
    }

    public  void exit() {
        exitFlag.set(true);
    }

    public  boolean getExit() {
        return exitFlag.get();
    }

    public  void in(String str) {
        while (!get()) {
            if (getExit()) {
                return;
            }
            synchronized (getObject()) {
                getObject().notifyAll();
            }
        }
        byteArrayInputStream = new ByteArrayInputStream((str + "\n").getBytes());
        System.setIn(byteArrayInputStream);
        set(false);
    }
}
