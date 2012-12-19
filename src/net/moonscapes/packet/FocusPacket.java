package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.*;
import net.moonscapes.phys.*;

/**
 */
public class FocusPacket implements Packet {
    private Vec2f pos;

    /**
     */
    public FocusPacket() {
    }

    /**
     */
    public FocusPacket(Vec2f pos) {
	this.pos = pos;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeFloat(pos.x);
	out.writeFloat(pos.y);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	pos = new Vec2f(in.readFloat(), in.readFloat());
    }

    /**
     */
    public Vec2f getPos() {
	return pos;
    }
}
