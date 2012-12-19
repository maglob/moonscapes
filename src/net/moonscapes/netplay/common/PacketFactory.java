package net.moonscapes.netplay.common;

import java.io.*;
import net.moonscapes.netplay.*;

/**
 */
public interface PacketFactory {
    /**
     */
    public Packet readPacket(DataInputStream in) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     */
    public void writePacket(Packet packet, DataOutputStream out) throws IOException;

    /**
     */
    public PacketNameMapper getNameMapper();
}
