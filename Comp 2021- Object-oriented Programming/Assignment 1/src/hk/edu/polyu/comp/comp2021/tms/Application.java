package hk.edu.polyu.comp.comp2021.tms;

import hk.edu.polyu.comp.comp2021.tms.model.TMS;

/**
 * The main application class.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class Application {

    /**
     * The main method that serves as the entry point for the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args){
        TMS tms = new TMS();
        tms.activate();
    }

}
