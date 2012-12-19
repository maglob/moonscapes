package net.moonscapes.netplay;

import net.moonscapes.netplay.client.*;
import net.moonscapes.netplay.server.*;

/**
 * Factory class for creating Netplay clients and servers
 */
public class Netplay {    

    /**
     */
    public static NetplayServer createServer(int port, ServerHandler handler) throws NetplayException {
	return new NetplayServerImpl(port, handler);
    }

    /**
     */
    public static NetplayClient createClient(String host, int port, ClientHandler handler) throws NetplayException {
	return new NetplayClientImpl(host, port, handler);
    }
}
