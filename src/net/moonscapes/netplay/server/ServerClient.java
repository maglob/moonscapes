package net.moonscapes.netplay.server;

import java.io.*;
import java.net.*;
import net.moonscapes.netplay.*;

/**
 */
class ServerClient implements Client {
    private int id;
    private Socket socket;
    private DataOutputStream out;
    private volatile boolean isConnected;

    /**
     */
    ServerClient(int id, Socket socket) {
	this.id = id;
	this.socket = socket;
	isConnected = true;
    }

    /**
     */
    public InetAddress getAddress() {
	return socket.getInetAddress();
    }

    /**
     */
    public int getPort() {
	return socket.getPort();
    }

    /**
     */
    DataOutputStream getDataOutput() throws IOException {
	if(out == null) {
	    synchronized(socket) {
		if(out == null)
		    out = new DataOutputStream(socket.getOutputStream());
	    }
	}
	return out;	
    }

    /**
     */
    public boolean equals(Object o) {
	return (o instanceof ServerClient) ? ((ServerClient)o).id==id : false;
    }

    /**
     */
    public int hashcode() {
	return id;
    }

    /**
     */
    public int getId() {
	return id;
    }

    /**
     */
    Socket getSocket() {
	return socket;
    }

    /**
     */
    public boolean isConnected() {
	return isConnected;
    }

    /**
     */
    public void disconnect() {
	try {
	    if(out != null)
		out.close();
	    if(socket != null)
		socket.close();
	} catch(IOException e) {
	    Log.error("ServerClient.disconnect(): "+ e);
	}
	isConnected = false;
    }

    /**
     */
    public String toString() {
	return ""+ id +", "+ socket.getInetAddress() +":"+ socket.getPort();
    }
}
