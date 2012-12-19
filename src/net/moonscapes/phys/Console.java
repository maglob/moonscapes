package net.moonscapes.phys;

/**
 * Console keymappings and control for player.
 *
 * @author Marko Aalto
 * @version $Id: Console.java,v 1.3 2001/11/11 17:09:10 mka Exp $
 */
public class Console {
    public static final int 
	THRUST   = 1,
	FIRE     = 2,
	SHIELD   = 4,
	LEFT     = 8,
	RIGHT    = 16,
	UP       = 32,
	DOWN     = 64,
	SELECT   = 128,
	OPTION_A = 256,
	OPTION_B = 512,
	CHAT     = 1024;
    private int status;

    /**
     */
    public Console() {
	this(0);
    }

    /**
     */
    public Console(int status) {
	this.status = status;
    }

    /**
     */
    public boolean getKey(int key) {
	return (status & key) != 0;
    }

    /**
     */
    public void setKey(int key, boolean isDown) {
	if(isDown)
	    status |= key;
	else
	    status &= key ^ 0xFFFFFFFF;
    }


    /**
     */
    public int getStatus() {
	return status;
    }

    /**
     */
    public void setStatus(int status) {
	this.status = status;
    }

    /**
     */
    public void clear() {
	setStatus(0);
    }
}

