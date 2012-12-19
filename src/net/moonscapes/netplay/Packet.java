package net.moonscapes.netplay;

import java.io.*;

/**
 * Basic unit in Netplay transpotation protocol. Netplay architecture
 * is build on top of Packets, which can be sent and received between
 * Server and Client.
 *
 * @author Marko Aalto
 * @version $Id: Packet.java,v 1.2 2001/11/11 17:09:03 mka Exp $
 * @see ClientHandler
 * @see ServerHandler
 */
public interface Packet {
    /**
     */
    public void write(DataOutputStream out) throws IOException;

    /**
     */
    public void read(DataInputStream in) throws IOException;
}
