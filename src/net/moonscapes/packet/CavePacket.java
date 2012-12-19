package net.moonscapes.packet;

import java.io.*;
import java.util.zip.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 */
public class CavePacket implements Packet {
    private byte[] bitmapData;
    private int width;
    private int height;
    private int scale;

    /**
     */
    public CavePacket() {
    }

    /**
     */
    public CavePacket(Bitmap bm, int scale) {
	bitmapData = bm.getBytes();
	width = bm.getWidth();
	height = bm.getHeight();
	this.scale = scale;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(width);
	out.writeInt(height);
	out.writeInt(scale);
	out.writeInt(bitmapData.length);
	byte[] b = new byte[bitmapData.length];
	Deflater def = new Deflater();
	def.setInput(bitmapData);
	def.finish();
	int n = def.deflate(b);
	out.writeInt(n);
	out.write(b, 0, n);

    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	width = in.readInt();
	height = in.readInt();
	scale = in.readInt();
	int n = in.readInt();
	bitmapData = new byte[n];
	n = in.readInt();
	byte b[] = new byte[n];
	in.read(b);

	Inflater inf = new Inflater();
	inf.setInput(b);
	try {
	    n = inf.inflate(bitmapData);
	} catch(DataFormatException e) {
	    System.err.println("CavePacket.read(): "+ e);
	    throw new IOException("CavePacket.read(): "+ e);
	}

    }


    /**
     */
    public int getWidth() {
	return width;
    }

    /**
     */
    public int getHeight() {
	return height;
    }

    /**
     */
    public int getScale() {
	return scale;
    }

    /**
     */
    public byte[] getBitmapBytes() {
	return bitmapData;
    }
}
