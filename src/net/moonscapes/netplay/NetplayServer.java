package net.moonscapes.netplay;

/**
 */
public interface NetplayServer {
    /**
     */
    public void start();

    /**
     * Sends packet unreliable.
     */
    public void sendPacket(Packet packet, Client client) throws NetplayException;

    /**
     * Sends packet (un)reliable.
     */
    public void sendPacket(Packet packet, Client client, boolean isReliable) throws NetplayException;

}
