package net.moonscapes.netplay;

import java.io.*;
import java.util.*;

/**
 * MetaPacket is group of normal packets, eg. you can send
 * many packets in one (UDP) packe.
 *
 * @author Marko Aalto
 * @version $Id: MetaPacket.java,v 1.1 2001/11/11 17:10:37 mka Exp $
 * @see Packet
 */
public class MetaPacket implements Packet {
    private Vector packets;

    /**
     */
    public MetaPacket() {
	packets = new Vector();
    }

    /**
     */
    public void addPacket(Packet p) {
	packets.addElement(p);
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	Log.error("MetaPacket.write(): this should not be called");
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	Log.error("MetaPacket.read(): this should not be called");
    }



    /**
     */
    public Enumeration getPackets() {
	return packets.elements();
    }

    /**
     */
    public int getPacketCount() {
	return packets.size();
    }
}
