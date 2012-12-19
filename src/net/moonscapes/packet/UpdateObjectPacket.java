package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 */
public class UpdateObjectPacket implements Packet {
    private int id;
    private Vec2f pos;
    private float dir;

    /**
     */
    public UpdateObjectPacket() {
    }

    /**
     */
    public UpdateObjectPacket(MSObject obj) {
	id = obj.getId();
	pos = obj.getPos();
	dir = obj.getDir();
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(id);
	out.writeFloat(pos.x);
	out.writeFloat(pos.y);
	out.writeFloat(dir);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	id = in.readInt();
	pos = new Vec2f(in.readFloat(), in.readFloat());	
	dir = in.readFloat();
    }

    /**
     */
    public int getId() {
	return id;
    }

    /**
     */
    public Vec2f getPos() {
	return pos;
    }

    /**
     */
    public float getDir() {
	return dir;
    }
}
