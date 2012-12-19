package net.moonscapes.netplay;

/**
 * Application program must provider ClientHandler to Netplay. Basicly
 * client will extend this interface.
 *
 * @author Marko Aalto
 * @version $Id: ClientHandler.java,v 1.2 2001/11/11 11:56:36 mka Exp $
 */
public interface ClientHandler {
    /**
     */
    public void handlePacket(Packet packet);

    /**
     */
    public void serverDisconnect();

}
