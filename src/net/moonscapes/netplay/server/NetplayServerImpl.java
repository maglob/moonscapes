package net.moonscapes.netplay.server;

import java.io.*;
import java.net.*;
import java.util.*;
import net.moonscapes.netplay.*;
import net.moonscapes.netplay.common.*;

/**
 * Actual implementation of the NetplayServer interface provided to the
 * application programmer.
 *
 * @author Marko Aalto
 * @version $Id: NetplayServerImpl.java,v 1.7 2001/11/11 17:09:08 mka Exp $
 */
public class NetplayServerImpl implements NetplayServer, Runnable, PacketHandler {
    private ServerSocket serverSocket;
    private ServerHandler serverHandler;
    private volatile boolean isRunning;
    private int nextClientId;
    private Hashtable addressToClient;
    private ServerPacketFactory packetFactory;
     private DatagramSocket datagramSocket;
    private DatagramSender datagramSender;
    


    /**
     */
    public NetplayServerImpl(int port, ServerHandler handler) throws NetplayException {	
	this.serverHandler = handler;
	try {
	    serverSocket = new ServerSocket(port);
	    datagramSocket = new DatagramSocket(port);
	} catch(IOException e) {
	    throw new NetplayException(e);
	}	
	nextClientId = 1;
	addressToClient = new Hashtable();
	packetFactory = new ServerPacketFactory();
	datagramSender = new DatagramSender(datagramSocket, packetFactory);
	Log.log("server listening to port "+ serverSocket.getLocalPort());
    }


    /**
     */
    public void start() {
	DatagramListener dl = new DatagramListener(datagramSocket, 
						   packetFactory, this);
	Thread t = new Thread(dl, "client udp");
	t.setDaemon(true);
	t.start();

	t = new Thread(this, "listen");
	t.setDaemon(true);
	isRunning = true;
	t.start();
    }


    /**
     * Separate thread listening ServerSocket for new connections.
     */
    public void run() {
	while(isRunning) {
	    try {
		Socket socket = serverSocket.accept();
		int cmd = socket.getInputStream().read();
		switch(cmd) {
		case Command.LOGIN:           cmdLogin(socket); break;
		case Command.ASK_PACKET_ID:   cmdAskPacketId(socket); break;
		case Command.ASK_PACKET_NAME: cmdAskPacketName(socket); break;
		default:
		    Log.error("NetplayServerImpl: handleConnection(): unknown command: "+ cmd);
		}
	    } catch(IOException e) {
		Log.error("NetplayServerImpl.run(): "+ e);
	    }
	}
    }

    /**
     */
    private void cmdLogin(Socket socket)  {
	ServerClient client = new ServerClient(getNextClientId(), socket);
	AddressPort addr = new AddressPort(socket.getInetAddress(), 
					   socket.getPort());
	addressToClient.put(addr, client);
	Listener list = new Listener(socket, packetFactory, this);
	Thread t = new Thread(list, "client "+ client.getId());
	Log.log("new client connected: "+ client);
	serverHandler.clientConnect(client);
	t.start();
    }

    /**
     */
    private void cmdAskPacketId(Socket socket) throws IOException {
	DataInputStream in = new DataInputStream(socket.getInputStream());
	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	String name = in.readUTF();
	int id = packetFactory.getNameMapper().getId(name);
	if(id < 0) {
	    try {
		Class cl = Class.forName(name);
		Packet p = (Packet)cl.newInstance();
		id = packetFactory.getPacketId(p);
	    } catch(Exception e) {
		Log.error("NetplayServerImpl.cmdAskPacketId(): "+ e);
	    }
	}
	if(id >= 0) {
	    out.writeInt(id);
	} else {
	    Log.error("NetplayServerImpl.cmdAskPacketId(): unknown packet name: "+ name);
	    out.writeInt(-1);
	}
	in.close();
	out.close();
	socket.close();
    }


    /**
     */
    private void cmdAskPacketName(Socket socket) throws IOException {
	DataInputStream in = new DataInputStream(socket.getInputStream());
	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	int id = in.readInt();
	String name = packetFactory.getNameMapper().getName(id);
	if(name != null) {
	    out.writeUTF(name);
	} else {
	    Log.error("NetplayServerImpl.cmdAskPacketName(): unknown packet id: "+ name);
	    out.writeUTF("");
	}
	in.close();
	out.close();
	socket.close();
    }


    /**
     */
    private void handleNetplayPacket(NetplayPacket packet, ServerClient client) {
	if(packet instanceof ClosePacket) {	    
	    client.disconnect();
	    serverHandler.clientDisconnect(client);
	} else
	    Log.error("NetplayServerImpl.handleNetplayPacket(): "+
		      "unknown packet: "+ packet);
    }


    /**
     */
    public void handlePacket(Packet packet, AddressPort source) {
	ServerClient client = (ServerClient)addressToClient.get(source);
	if(client != null) {
	    if(packet instanceof NetplayPacket) 
		handleNetplayPacket((NetplayPacket)packet, client);
	    else if(packet instanceof MetaPacket) {
		Enumeration e = ((MetaPacket)packet).getPackets();
		while(e.hasMoreElements())
		    serverHandler.handlePacket((Packet)e.nextElement(), client);
	    } else
		serverHandler.handlePacket(packet, client);
	} else {
	    Log.error("NetPlayServerImpl.handlePacket(): unknown address: "+ source);
	}
    }

    /**
     */
    private synchronized int getNextClientId() {
	return nextClientId++;
    }

    /**
     * Sends packet unreliable.
     */
    public void sendPacket(Packet packet, Client client) throws NetplayException {
	sendPacket(packet, client, false);
    }

    /**
     * Sends packet (un)reliable.
     */
    public void sendPacket(Packet packet, Client client, boolean isReliable) throws NetplayException {
	ServerClient sc = (ServerClient)client;
	try {
	    if(isReliable) {
		packetFactory.writePacket(packet, sc.getDataOutput());
	    } else {
		datagramSender.sendPacket(packet, sc.getSocket().getInetAddress(), sc.getSocket().getPort());
	    }
	} catch(IOException e) {
	    throw new NetplayException(e);
	}
    }

}
