package util;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used to assist in testing the game by providing a way to simulate user input.
 * @author Liu Yuyang
 */

public class InputUtil {

    private static ConcurrentHashMap<Thread, InputFlagUtil> map = new ConcurrentHashMap<>();

    public static void set(boolean flag) {
        map.getOrDefault(Thread.currentThread(),new InputFlagUtil()).set(flag);
    }

    public static Boolean get() {
        return map.getOrDefault(Thread.currentThread(),new InputFlagUtil()).get();
    }

    public static String next() {
        if (map.get(Thread.currentThread())==null){
            Scanner nameScanner = new Scanner(System.in);
            return nameScanner.next();
        }
        return map.getOrDefault(Thread.currentThread(),new InputFlagUtil()).next();
    }

    public static Object getObject() {
        return map.getOrDefault(Thread.currentThread(),new InputFlagUtil()).getObject();
    }

    public static void exit() {
        map.getOrDefault(Thread.currentThread(),new InputFlagUtil()).exit();
    }

    public static boolean getExit() {
        return map.getOrDefault(Thread.currentThread(),new InputFlagUtil()).getExit();
    }

    public static void in(String str) {
        map.getOrDefault(Thread.currentThread(),new InputFlagUtil()).in(str);
    }

    public static void put(Thread thread, InputFlagUtil inputFlagUtil) {
        map.put(thread, inputFlagUtil);
    }
}
