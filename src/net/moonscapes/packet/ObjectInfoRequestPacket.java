package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.*;


/**
 */
public class ObjectInfoRequestPacket implements Packet {
    private int objectId;

    /**
     */
    public ObjectInfoRequestPacket() {
    }


    /**
     */
    public ObjectInfoRequestPacket(int objectId) {
	this.objectId = objectId;
    }


    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(objectId);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	objectId = in.readInt();
    }

    /**
     */
    public int getObjectId() {
	return objectId;
    }
}
