package net.moonscapes.test.datagramsocket;

import java.io.*;
import java.net.*;

public class Client implements Runnable {
    private Socket tcpSocket;
    private DatagramSocket dgSocket;
    private int type;

    public static void main(String args[]) throws Exception {
	Client s = new Client();
	Thread t1 = new Thread(s, "tcp");
	Thread t2 = new Thread(s, "udp");
	t1.start();
	t2.start();
    }

    private Client() throws Exception {
	tcpSocket = new Socket("localhost", 9010);
	dgSocket = new DatagramSocket(tcpSocket.getLocalPort());
    }

    public void run() {
	Thread t = Thread.currentThread();
	if(t.getName().equals("tcp")) {
	    try {
		OutputStream out = tcpSocket.getOutputStream();
		while(true) {
		    out.write("testi".getBytes());
		    Thread.sleep((long)(Math.random()*3000));		    
		}
	    } catch(Exception e) {
		System.err.println("tcp: "+ e);
	    }
	} else if(t.getName().equals("udp")) {
	    try {
		while(true) {
		    byte[] b = "test udp".getBytes();
		    DatagramPacket packet = new DatagramPacket(b, b.length);
		    packet.setAddress(tcpSocket.getInetAddress());
		    packet.setPort(9010);
		    dgSocket.send(packet);
		    Thread.sleep((long)(Math.random()*3000)); 
		}
	    } catch(Exception e) {
		System.err.println("udp: "+ e);
	    }
	} else {
	    System.err.println("Client.run(): unknown thread name");
	}
    }
	    
}
