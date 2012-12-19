package net.moonscapes.packet;

import java.io.*;
import java.util.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 * Update a group of objects in one packet.
 */
public class UpdateObjectsPacket implements Packet {
    private Vector objects;

    /**
     */
    public UpdateObjectsPacket() {
	this(null);
    }

    /**
     */
    public UpdateObjectsPacket(Vector objects) {
	this.objects = objects;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(objects.size());
	for(int i=0; i<objects.size(); i++) {
	    MSObject o = (MSObject)objects.elementAt(i);
	    out.writeInt(o.getId());
	    out.writeShort((short)(o.getPos().x + .5f));
	    out.writeShort((short)(o.getPos().y + .5f));
	    float f = (float)Math.IEEEremainder(o.getDir(), Math.PI*2);
	    out.writeShort((short)(f * 1024));
	}	    
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	int n = in.readInt();
	objects = new Vector(n);
	for(int i=0; i<n; i++) {
	    MSObject o = new MSObject(in.readInt());
	    o.setPos(new Vec2f(in.readShort(), in.readShort()));
	    //float f = in.readFloat();	    
	    float f = in.readShort() / 1024f;
	    o.setDir(f);
	    objects.addElement(o);
	}
    }


    /**
     */
    public Vector getObjects() {
	return objects;
    }
}
