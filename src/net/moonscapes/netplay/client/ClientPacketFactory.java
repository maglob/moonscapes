package net.moonscapes.netplay.client;

import java.io.*;
import java.net.*;
import net.moonscapes.netplay.*;
import net.moonscapes.netplay.common.*;

/**
 * Client side packet factory, which does not issue new packet ids,
 * instead it will query necessary (previously unknown) packet ids
 * from the server (which has the authority over packet ids).
 *
 * @author Marko Aalto
 * @version $Id: ClientPacketFactory.java,v 1.1 2001/10/24 18:04:37 mka Exp $
 */
class ClientPacketFactory extends BasePacketFactory {
    private AddressPort server;

    /**
     * Dont allow null constructor.
     */
    private ClientPacketFactory() {
    }

    /**
     */
    ClientPacketFactory(AddressPort server) {
	this.server = server;
    }
    
    /**
     */
    protected int getPacketId(Packet packet) {
	String name = packet.getClass().getName();
	int id = nameMapper.getId(name);
	if(id < 0) {
	    id = askPacketId(name);
	    if(id >= 0)
		nameMapper.addName(name, id);
	}
	return id;
    }


    /**
     */
    protected String getPacketName(int id) {
	String name = nameMapper.getName(id);
	if(name == null) {
	    name = askPacketName(id);
	    if(name != null)
		nameMapper.addName(name, id);
	}
	return name;
    }


    /**
     * Ask packet name from the server
     */
    private String askPacketName(int id) {
	try {
	    Socket s = new Socket(server.getAddress(), server.getPort());
	    DataOutputStream out = new DataOutputStream(s.getOutputStream());
	    DataInputStream in = new DataInputStream(s.getInputStream());
	    out.writeByte(Command.ASK_PACKET_NAME);
	    out.writeInt(id);
	    out.flush();
	    String name = in.readUTF();
	    out.close();
	    in.close(); 
	    s.close();
	    return name;
	} catch(IOException e) {
	    Log.error("ClientPacketFactory.askPacketName(): "+ e);
	    return null;
	}
    }


    /**
     * Ask packet id from the server.
     */
    private int askPacketId(String name) {
	try {
	    Socket s = new Socket(server.getAddress(), server.getPort());
	    DataOutputStream out = new DataOutputStream(s.getOutputStream());
	    DataInputStream in = new DataInputStream(s.getInputStream());
	    out.writeByte(Command.ASK_PACKET_ID);
	    out.writeUTF(name);
	    out.flush();
	    int id = in.readInt();
	    out.close();
	    in.close(); 
	    s.close();
	    return id; 
	} catch(IOException e) {
	    Log.error("ClientPacketFactory.askPacketId(): "+ e);
	    return -1;
	}
    }


}
