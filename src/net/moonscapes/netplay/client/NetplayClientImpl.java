package net.moonscapes.netplay.client;

import java.io.*;
import java.net.*;
import java.util.*;
import net.moonscapes.netplay.*;
import net.moonscapes.netplay.common.*;

/**
 */
public class NetplayClientImpl implements NetplayClient, PacketHandler {
    private Socket socket;
    private DatagramSocket datagramSocket;
    private DataOutputStream out;
    private ClientHandler clientHandler;
    private volatile boolean isRunning;
    private PacketFactory packetFactory;
    private DatagramSender datagramSender;
    private DatagramListener datagramListener;
    private Thread dglThread;
    private volatile boolean isConnected;


    /**
     */
    public NetplayClientImpl(String host, int port, ClientHandler handler) throws NetplayException {	
	this.clientHandler = handler;
	try {
	    socket = new Socket(host, port);	   
	    datagramSocket = new DatagramSocket(socket.getLocalPort());
	    out = new DataOutputStream(socket.getOutputStream());	    
	} catch(IOException e) {
	    throw new NetplayException(e);
	}	

	packetFactory = new ClientPacketFactory(new AddressPort(socket.getInetAddress(), socket.getPort()));
	datagramSender = new DatagramSender(datagramSocket, packetFactory);

	Log.log("client connected to server: "+ socket.getInetAddress() +":"+
		socket.getPort());
    }


    /**
     */
    public void connect() throws NetplayException {
	datagramListener = new DatagramListener(datagramSocket, packetFactory, this);
	dglThread = new Thread(datagramListener, "server udp");
	dglThread.setDaemon(true);
	dglThread.start();

	Listener list = new Listener(socket, packetFactory, this);
	Thread t = new Thread(list, "server tcp");
	t.setDaemon(true);
	t.start();
	try {
	    out.writeByte(Command.LOGIN);
	    out.flush();
	} catch(IOException e) {
	    throw new NetplayException(e);
	}
	isConnected = true;
    }

    /**
     */
    public boolean isConnected() {
	return isConnected;
    }


    /**
     */
    private void handleNetplayPacket(NetplayPacket packet) {
	if(packet instanceof ClosePacket) {
	    //client.setConnected(false);
	    datagramListener.stop();
	    datagramSocket.close();
	    //dglThread.interrupt();
	    try {
		socket.close();
	    } catch(IOException e) {
		Log.error("NetplayClientImpl.handleNetplayPacket(): "+ e);
	    }
	    isConnected = false;
	    clientHandler.serverDisconnect();
	} else
	    Log.error("NetplayClientImpl.handleNetplayPacket(): "+
		      "unknown packet: "+ packet);
    }


    /**
     */
    public void handlePacket(Packet packet, AddressPort source) {	
	if(packet instanceof NetplayPacket)
	    handleNetplayPacket((NetplayPacket)packet);
	else if(packet instanceof MetaPacket) {
	    Enumeration e = ((MetaPacket)packet).getPackets();
	    while(e.hasMoreElements())
		clientHandler.handlePacket((Packet)e.nextElement());
	} else
	    clientHandler.handlePacket(packet);
    }


    /**
     * Sends packet unreliable.
     */
    public void sendPacket(Packet packet) throws NetplayException {
	sendPacket(packet, false);
    }

    /**
     * Sends packet (un)reliable.
     */
    public void sendPacket(Packet packet, boolean isReliable) throws NetplayException {	
	try {
	    if(isReliable) {
		packetFactory.writePacket(packet, out);
	    } else {
		datagramSender.sendPacket(packet, socket.getInetAddress(), 
					  socket.getPort());
	    }
	} catch(IOException e) {
	    throw new NetplayException(e);
	}

    }

}
