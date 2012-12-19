package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 */
public class ShipAttributesPacket implements Packet {
    private int id;
    private boolean isShieldOn;
    private boolean isThrustOn;
    private byte energy, maxEnergy;
    private Vec2f vel;

    /**
     */
    public ShipAttributesPacket() {
    }

    /**
     */
    public ShipAttributesPacket(MSObject obj, boolean shield, float ener,
				float maxEner, boolean thrust, Vec2f vel) {
	id = obj.getId();
	isShieldOn = shield;
	energy = (byte)ener;
	maxEnergy = (byte)maxEner;
	isThrustOn = thrust;
	this.vel = vel;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(id);
	out.writeBoolean(isShieldOn);
	out.writeByte(energy);
	out.writeByte(maxEnergy);
	out.writeBoolean(isThrustOn);
	out.writeFloat(vel.x);
	out.writeFloat(vel.y);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	id = in.readInt();
	isShieldOn = in.readBoolean();
	energy = in.readByte();
	maxEnergy = in.readByte();
	isThrustOn = in.readBoolean();
	vel = new Vec2f(in.readFloat(), in.readFloat());
    }

    /**
     */
    public int getId() {
	return id;
    }

    /**
     */
    public boolean isShieldOn() {
	return isShieldOn;
    }

    /**
     */
    public int getEnergy() {
	return energy;
    }

    /**
     */
    public int getMaxEnergy() {
	return maxEnergy;
    }

    /**
     */
    public boolean isThrustOn() {
	return isThrustOn;
    }

    /**
     */
    public Vec2f getVelocity() {
	return vel;
    }
}

