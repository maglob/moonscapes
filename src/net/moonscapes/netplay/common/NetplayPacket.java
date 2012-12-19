package net.moonscapes.netplay.common;

import java.io.*;
import net.moonscapes.netplay.*;

/**
 * Base class for packets used internally by Netplay. Applicatio
 * programmers should not use these packets.
 *
 * @author Marko Aalto
 * @version $Id: NetplayPacket.java,v 1.1 2001/11/10 10:38:17 mka Exp $
 */
public abstract class NetplayPacket implements Packet {
    /**
     */
    public void write(DataOutputStream out) {
    }

    public void read(DataInputStream in) {
    }
}
