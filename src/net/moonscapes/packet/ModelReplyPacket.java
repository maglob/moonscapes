package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.*;
import net.moonscapes.phys.*;

/**
 */
public class ModelReplyPacket implements Packet {
    private String modelName;
    private Model model;

    /**
     */
    public ModelReplyPacket() {
    }

    /**
     */
    public ModelReplyPacket(String modelName, Model model) {
	this.modelName = modelName;
	this.model = model;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeUTF(modelName);
	out.writeInt(model.getDirs());
	Vec2f[] v = model.getVertices();
	out.writeInt(v.length);
	for(int i=0; i<v.length; i++) {
	    out.writeFloat(v[i].x);
	    out.writeFloat(v[i].y);
	}
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	modelName = in.readUTF();
	int dirs = in.readInt();
	int n = in.readInt();
	Vec2f[] v = new Vec2f[n];
	for(int i=0; i<n; i++) 
	    v[i] = new Vec2f(in.readFloat(), in.readFloat());
	model = new Model(modelName, v, dirs);
    }

    /**
     */
    public String getModelName() {
	return modelName;
    }

    /**
     */
    public Model getModel() {
	return model;
    }
}
