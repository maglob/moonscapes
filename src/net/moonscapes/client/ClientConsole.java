package net.moonscapes.client;

import java.awt.event.*;
import net.moonscapes.phys.Console;

/**
 * Client side console listening to key events
 *
 * @author Marko Aalto
 * @version $Id: ClientConsole.java,v 1.2 2001/11/03 01:20:43 mka Exp $
 * @see net.moonscapes.phys.Console
 */
class ClientConsole extends Console implements KeyListener {
    private int keyValues[];
    private StringBuffer chatBuffer;
    private String chatText;

    /**
     */
    ClientConsole() {
	super();
    }

    /**
     */
    private void updateKey(int key, boolean isDown) {
	switch(key) {
	case KeyEvent.VK_A: setKey(LEFT, isDown); break;
	case KeyEvent.VK_S: setKey(RIGHT, isDown); break;
	case KeyEvent.VK_SHIFT: setKey(THRUST, isDown); break;
	case KeyEvent.VK_ENTER: setKey(FIRE, isDown); break;
	case KeyEvent.VK_SPACE: setKey(SHIELD, isDown); break;
	case KeyEvent.VK_T: 
	    if(chatBuffer == null)
		chatBuffer = new StringBuffer();
	    setKey(CHAT, isDown); 
	    break;
	}
    }

    /**
     */
    public void keyPressed(KeyEvent e) {
	if(chatBuffer != null) {
	    switch(e.getKeyCode()) {
	    case KeyEvent.VK_ESCAPE: 
		chatBuffer = null;
		break;
	    case KeyEvent.VK_ENTER:
		chatText = chatBuffer.toString();
		chatBuffer = null;
		break;
	    case KeyEvent.VK_DELETE:
	    case KeyEvent.VK_BACK_SPACE:
		int n = chatBuffer.length();
		if(n > 0)
		    chatBuffer.setLength(n - 1);
		break;
	    default:
		char c = e.getKeyChar();
		if(!Character.isISOControl(c))
		    chatBuffer.append(c);
	    }
	} else
	    updateKey(e.getKeyCode(), true);
    }

    /**
     */
    public void keyReleased(KeyEvent e) {
	updateKey(e.getKeyCode(), false);
    }

    /**
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     */
    public String getChatBuffer() {
	return chatBuffer==null ? null : chatBuffer.toString();
    }

    /**
     */
    public String getChatText() {
	return chatText;
    }

    /**
     */
    public void clearChat() {
	chatText = null;
    }
}
