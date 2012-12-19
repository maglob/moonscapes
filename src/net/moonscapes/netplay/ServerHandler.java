package net.moonscapes.netplay;

/**
 */
public interface ServerHandler {
    /**
     */
    public void clientConnect(Client client);

    /**
     */
    public void clientDisconnect(Client client);

    /**
     */
    public void handlePacket(Packet packet, Client source);
}
