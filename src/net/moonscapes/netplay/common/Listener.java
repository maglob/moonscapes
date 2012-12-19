package net.moonscapes.netplay.common;

import java.io.*;
import java.net.*;
import net.moonscapes.netplay.*;

/**
 * Listening single stream socket for incoming data. Used both
 * in client and server implementation.
 */
public class Listener implements Runnable {
    private Socket socket;
    private PacketFactory packetFactory;
    private PacketHandler packetHandler;
    private volatile boolean isRunning;

    /*
     */
    public Listener(Socket socket, PacketFactory packetFactory, PacketHandler packetHandler) {
	this.socket = socket;
	this.packetFactory = packetFactory;
	this.packetHandler = packetHandler;
	isRunning = true;
    }

    /**
     */
    public void run() {
	DataInputStream dis = null;
	try {
	    dis = new DataInputStream(socket.getInputStream());
	} catch(IOException e) {
	    Log.error("Listener.run(): "+ e);
	    isRunning = false;
	}
	AddressPort source = new AddressPort(socket.getInetAddress(), 
					     socket.getPort());
	while(isRunning) {
	    try {
		Packet p = packetFactory.readPacket(dis);
		packetHandler.handlePacket(p, source);
	    } catch(EOFException ee) {
		Log.log("connection lost (received EOF)");
		isRunning = false;
	    } catch(IOException e) {
		Log.error("Listener.run() 1: "+ e);
	    } catch(ClassNotFoundException ce) {
		Log.error("Listener.run() 2: "+ ce);
	    } catch(InstantiationException ie) {
		Log.error("Listener.run() 3: "+ ie);
	    } catch(IllegalAccessException iae) {
		Log.error("Listener.run() 4: "+ iae);
	    }
	}	
	try {
	    dis.close();
	} catch(IOException e) {
	    Log.error("Listener.run() 10:"+ e);
	}
	Log.log("listener thread ended from "+ source);
	packetHandler.handlePacket(new ClosePacket(), source);
    }

    /**
     */
    public void stop() {
	isRunning = false;	
    }
}
