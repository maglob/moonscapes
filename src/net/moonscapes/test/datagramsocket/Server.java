package net.moonscapes.test.datagramsocket;

import java.io.*;
import java.net.*;

public class Server implements Runnable {
    private ServerSocket tcpSocket;
    private DatagramSocket dgSocket;
    private int type;

    public static void main(String args[]) throws Exception {
	Server s = new Server();
	Thread t1 = new Thread(s, "tcp");
	Thread t2 = new Thread(s, "udp");
	t1.start();
	t2.start();
    }

    private Server() throws Exception {
	tcpSocket = new ServerSocket(9010);
	dgSocket = new DatagramSocket(9010);
    }

    public void run() {
	Thread t = Thread.currentThread();
	if(t.getName().equals("tcp")) {
	    try {
		System.out.println("tcp: before accept");
		Socket s = tcpSocket.accept();
		System.out.println("tcp: after accept");
		InputStream in = s.getInputStream();
		int b = 0;
		while(b >= 0) {
		    System.out.println("tcp: before read");
		    b = in.read();
		    System.out.println("tcp: after read: "+ b);
		}
		System.out.println("tcp: closed");
		s.close();
	    } catch(Exception e) {
		System.err.println("tcp: "+ e);
	    }
	} else if(t.getName().equals("udp")) {
	    try {
		while(true) {
		    DatagramPacket packet = new DatagramPacket(new byte[100], 100);
		    System.out.println("udp: before receive");
		    dgSocket.receive(packet);
		    System.out.println("udp: after receive");
		}
	    } catch(Exception e) {
		System.err.println("udp: "+ e);
	    }
	} else {
	    System.err.println("Server.run(): unknown thread name");
	}
    }
	    
}
