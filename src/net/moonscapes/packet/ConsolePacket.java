package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.*;


/**
 */
public class ConsolePacket implements Packet {
    private int status;

    /**
     */
    public ConsolePacket() {
    }

    /**
     */
    public ConsolePacket(int status) {
	this.status = status;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(status);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	status = in.readInt();
    }

    /**
     */
    public int getStatus() {
	return status;
    }

    /**
     */
    public void setStatus(int status) {
	this.status = status;
    }
}
