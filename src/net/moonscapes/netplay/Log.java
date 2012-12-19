package net.moonscapes.netplay;

import java.io.*;
import java.util.*;
import java.text.*;


/**
 * Log writer utility class
 */
public class Log {
    private static PrintStream log = System.out;
    private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static boolean 
	doDebug = System.getProperty("DEBUG","1").equals("1"),
	doError = true,
	doLog = true,
	doWarning = true;

    /**
     */
    private static void write(String s) {
	log.println(df.format(new Date()) +" ["+ 
		    Thread.currentThread().getName() +"] "+ s);
    }

    /**
     */
    public static void log(String s) {
	if(doLog)
	    write(s);
    }

    /**
     */
    public static void debug(String s) {
	if(doDebug)
	    write("DEBUG "+ s);
    }

    /**
     */
    public static void error(String s) {
	if(doError)
	    write("ERROR "+ s);
    }

    /**
     */
    public static void warning(String s) {
	if(doWarning)
	    write("WARNING "+ s);
    }


    /**
     */
    /* public static void log(Client c, String s) {
	log(""+ c.getId() +": "+ s);
	} */
}


