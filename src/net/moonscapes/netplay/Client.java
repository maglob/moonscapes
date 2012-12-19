package net.moonscapes.netplay;

import java.io.*;
import java.net.*;

/**
 * Application server's view to a client connected to the server.
 *
 * @author Marko Aalto
 * @version $Id: Client.java,v 1.5 2001/11/11 17:09:03 mka Exp $
 */
public interface Client {
    /**
     */
    public int getId();

    /**
     */
    public InetAddress getAddress();

    /**
     */
    public int getPort();

    /**
     */
    public boolean isConnected();

    /**
     */
    public void disconnect();
}
