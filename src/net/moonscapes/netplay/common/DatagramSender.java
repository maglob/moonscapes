package net.moonscapes.netplay.common;

import java.io.*;
import java.net.*;
import net.moonscapes.netplay.*;

/**
 * Utility class to send Packet as UPD packets.
 *
 * @author Marko Aalto
 * @version $Id: DatagramSender.java,v 1.1 2001/10/24 18:04:38 mka Exp $
 */
public class DatagramSender extends ByteArrayOutputStream {
    public final static int BUFFER_SIZE = 8192;
    private DatagramPacket dgPacket;
    private DatagramSocket socket;
    private DataOutputStream out;
    private PacketFactory packetFactory;

    /**
     */
    public DatagramSender(DatagramSocket socket, PacketFactory packetFactory) {
	super(BUFFER_SIZE);
	this.socket = socket;
	this.packetFactory = packetFactory;
	dgPacket = new DatagramPacket(buf, buf.length);
	out = new DataOutputStream(this);
    }

    /**
     */
    public void sendPacket(Packet packet, InetAddress addr, int port) throws IOException {
	out.flush();
	reset();
	packetFactory.writePacket(packet, out);
	dgPacket.setData(buf);
	dgPacket.setLength(size());
	dgPacket.setAddress(addr);
	dgPacket.setPort(port);
	socket.send(dgPacket);	
	//Log.debug("DatagramSender.sendPacket(): sent "+ size() +" bytes to "+
	//	  dgPacket.getAddress() +":"+ dgPacket.getPort());
    }

}
