package net.moonscapes.packet;

import java.io.*;
import java.util.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 */
public class UpdateShotsPacket implements Packet {
    private Vector positions;

    /**
     */
    public UpdateShotsPacket() {
	this(null);
    }

    /**
     */
    public UpdateShotsPacket(Vector positions) {
	this.positions = positions;
    }


    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(positions.size());
	for(int i=0; i<positions.size(); i++) {
	    Vec2f v = (Vec2f)positions.elementAt(i);
	    out.writeShort((short)v.x);
	    out.writeShort((short)v.y);
	}
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	int n = in.readInt();
	positions = new Vector(n);
	for(int i=0; i<n; i++) 
	    positions.addElement(new Vec2f(in.readShort(), in.readShort()));
    }

    /**
     */
    public Vector getShots() {
	return positions;
    }

	
}
