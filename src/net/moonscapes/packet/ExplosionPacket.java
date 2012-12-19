package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 */
public class ExplosionPacket implements Packet {
    private Vec2f pos;
    private float force;
    private short nspark, ndebris;
    private int objId;

    /**
     */
    public ExplosionPacket() {
    }

    /**
     * @param objId zero means no object assigned
     */
    public ExplosionPacket(Vec2f pos, float force, int nspark, int ndebris,
			   int objId) {
	this.pos = pos;
	this.force = force;
	this.nspark = (short)nspark;
	this.ndebris = (short)ndebris;
	this.objId = objId;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeFloat(pos.x);
	out.writeFloat(pos.y);
	out.writeFloat(force);
	out.writeShort(nspark);
	out.writeShort(ndebris);
	out.writeInt(objId);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	pos = new Vec2f(in.readFloat(), in.readFloat());	
	force = in.readFloat();
	nspark = in.readShort();
	ndebris = in.readShort();
	objId = in.readInt();
    }


    /**
     */
    public Vec2f getPos() {
	return pos;
    }

    /**
     */
    public float getForce() {
	return force;
    }

    /**
     */
    public int getSparkCount() {
	return nspark;
    }

    /**
     */
    public int getDebrisCount() {
	return ndebris;
    }

    /**
     */
    public int getObjectId() {
	return objId;
    }
}
