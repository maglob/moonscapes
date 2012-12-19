package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.*;


/**
 */
public class TestPacket implements Packet {
    private String text;

    /**
     */
    public TestPacket() {
    }

    /**
     */
    public TestPacket(String s) {
	text = s;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeUTF(text);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	text = in.readUTF();
    }

    /**
     */
    public String getText() {
	return text;
    }

    /**
     */
    public String toString() {
	return text;
    }
}
