package net.moonscapes.packet;

import java.io.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 */
public class FramePacket implements Packet {
    private long frame, time;


    /**
     */
    public FramePacket() {
    }

    /**
     */
    public FramePacket(long frame, long time) {
	this.frame = frame;
	this.time = time;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeLong(frame);
	out.writeLong(time);
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	frame = in.readLong();
	time = in.readLong();
    }


    /**
     */
    public long getFrame() {
	return frame;
    }

    /**
     */
    public long getTime() {
	return time;
    }
}
