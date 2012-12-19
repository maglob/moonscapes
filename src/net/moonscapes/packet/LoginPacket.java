package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.Packet;

/**
 */
public class LoginPacket implements Packet {
    private String name;

    /**
     */
    public LoginPacket() {
    }

    /**
     */
    public LoginPacket(String name) {
	this.name = name;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeUTF(name);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	name = in.readUTF();
    }

    /**
     */
    public String getName() {
	return name;
    }

}
