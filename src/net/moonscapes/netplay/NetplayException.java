package net.moonscapes.netplay;

/**
 */
public class NetplayException extends Exception {
    private Exception nested;

    /**
     */
    public NetplayException(String s) {
	super(s);
    }

    /**
     */
    public NetplayException(Exception e) {
	nested = e;
    }

    /**
     */
    public Exception getNested() {
	return nested;
    }

    /**
     */
    public String toString() {
	String s = getClass().getName() + ": ";
	s += nested!=null ? nested.toString() : getMessage();
	return s;
    }
}
