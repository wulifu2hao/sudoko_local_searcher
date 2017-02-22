/**
 * Created by lifu.wu on 19/2/17.
 * A0105661M e0045348@u.nus.edu
 */
public class Printer {
    public static final boolean VERBOSE = false;

    public static void printIfVerbose(String content){
        if (VERBOSE) {
            System.out.print(content);
        }
    }

    public static void printlnIfVerbose(String content){
        if (VERBOSE) {
            System.out.println(content);
        }
    }

    public static void print(String content){
        System.out.print(content);
    }

    public static void println(String content){
        System.out.println(content);
    }
}
