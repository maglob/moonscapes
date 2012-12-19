package net.moonscapes.netplay.common;

import net.moonscapes.netplay.*;

/**
 */
public interface PacketHandler {
    /**
     */
    public void handlePacket(Packet packet, AddressPort source);
}
