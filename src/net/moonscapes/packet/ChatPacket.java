package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.Packet;

/**
 */
public class ChatPacket implements Packet {
    private String text;

    /**
     */
    public ChatPacket() {
    }

    /**
     */
    public ChatPacket(String text) {
	this.text = text;
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

}
