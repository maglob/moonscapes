package net.moonscapes.netplay.common;

import java.io.*;
import java.net.*;
import net.moonscapes.netplay.*;

/**
 * Listening datagram socket for incoming packets.
 *
 * @author Marko Aalto
 * @version $Id: DatagramListener.java,v 1.3 2001/11/11 11:56:38 mka Exp $
 */
public class DatagramListener implements Runnable {
    private static final int BUFFER_SIZE = 8192;
    private DatagramSocket socket;
    private DatagramPacket dgPacket;
    private PacketFactory packetFactory;
    private PacketHandler packetHandler;
    private volatile boolean isRunning;
    private byte[] buffer;

    /*
     */
    public DatagramListener(DatagramSocket socket, PacketFactory packetFactory, PacketHandler packetHandler) {
	this.socket = socket;
	this.packetFactory = packetFactory;
	this.packetHandler = packetHandler;
	buffer = new byte[BUFFER_SIZE];
	dgPacket = new DatagramPacket(buffer, buffer.length);
	isRunning = true;
    }


    /**
     */
    public void run() {
	Log.log("datagram listener started on port "+ socket.getLocalPort());
	while(isRunning) {
	    try {
		socket.setSoTimeout(1000);
		dgPacket.setData(buffer);
		dgPacket.setLength(buffer.length);
		socket.receive(dgPacket);
		ByteArrayInputStream bais = new ByteArrayInputStream(dgPacket.getData(), 0, dgPacket.getLength());
		DataInputStream in = new DataInputStream(bais);
		Packet p = packetFactory.readPacket(in);
		AddressPort source = new AddressPort(dgPacket.getAddress(), 
						     dgPacket.getPort());
		packetHandler.handlePacket(p, source);
		in.close();
		bais.close();
	    } catch(EOFException ee) {
		Log.error("DatagramListener.run() 1: "+ ee);
		isRunning = false;
	    } catch(InterruptedIOException iioe) {
		// Silently acknowldege SO timeout
		// Log.debug("DatagramListener.run() 2: "+ iioe);
	    } catch(IOException e) {
		Log.error("DatagramListener.run() 3: "+ e);
		//System.exit(0);
		//isRunning = false;
	    } catch(ClassNotFoundException ce) {
		Log.error("DatagramListener.run() 4: "+ ce);
	    } catch(InstantiationException ie) {
		Log.error("DatagramListener.run() 5: "+ ie);
	    } catch(IllegalAccessException iae) {
		Log.error("DatagramListener.run() 6: "+ iae);
	    }
	}	
	Log.log("datagramlistener thread ended: "+ 
		Thread.currentThread().getName());
    }

    /**
     */
    public void stop() {
	isRunning = false;
    }
}
