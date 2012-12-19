package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.*;


/**
 */
public class ObjectInfoReplyPacket implements Packet {
    private int objectId;
    private String modelName;
    private String title;

    /**
     */
    public ObjectInfoReplyPacket() {
    }

    /**
     */
    public ObjectInfoReplyPacket(int objectId, String modelName) {
	this(objectId, modelName, null);
    }


    /**
     */
    public ObjectInfoReplyPacket(int objectId, String modelName, String title) {
	this.objectId = objectId;
	this.modelName = modelName;
	this.title = title;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(objectId);
	out.writeUTF(modelName);
	out.writeUTF(title!=null ? title : "");
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	objectId = in.readInt();
	modelName = in.readUTF();
	title = in.readUTF();
	if(title.length() < 1)
	    title = null;
    }

    /**
     */
    public int getObjectId() {
	return objectId;
    }


    /**
     */
    public String getModelName() {
	return modelName;
    }

    /**
     */
    public String getTitle() {
	return title;
    }
}
