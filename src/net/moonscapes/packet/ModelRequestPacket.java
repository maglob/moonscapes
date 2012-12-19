package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.*;


/**
 */
public class ModelRequestPacket implements Packet {
    private String modelName;

    /**
     */
    public ModelRequestPacket() {
    }

    /**
     */
    public ModelRequestPacket(String modelName) {
	this.modelName = modelName;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeUTF(modelName);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	modelName = in.readUTF();
    }

    /**
     */
    public String getModelName() {
	return modelName;
    }
}
