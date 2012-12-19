package net.moonscapes.netplay;

/**
 */
public interface NetplayClient {

    /**
     */
    public void connect() throws NetplayException;

    /**
     */
    public boolean isConnected();

    /**
     * Sends packet unreliable. 
     */
    public void sendPacket(Packet packet) throws NetplayException;

    /**
     * Sends packet (un)reliable.
     */
    public void sendPacket(Packet packet, boolean isReliable) throws NetplayException;
}
