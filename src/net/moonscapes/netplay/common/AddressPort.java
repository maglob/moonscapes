package net.moonscapes.netplay.common;

import java.net.*;

/**
 * Utility class to combine InetAddress and port number, eg.
 * unique identification of socket communication end point.
 */
public class AddressPort {
    private InetAddress address;
    private int port;

    /**
     */
    public AddressPort(InetAddress address, int port) {
	this.address = address;
	this.port = port;
    }

    /**
     */
    public InetAddress getAddress() {
	return address;
    }

    /**
     */
    public int getPort() {
	return port;
    }


    /**
     */
    public boolean equals(Object o) {
	if(o instanceof AddressPort) {
	    AddressPort ap = (AddressPort)o;
	    return address.equals(ap.address) && port==ap.port;
	} else
	    return false;
    }

    /**
     */
    public int hashCode() {
	return address.hashCode() ^ port;
    }

    /**
     */
    public String toString() {
	return address.toString() +":"+ port;
    }
}
