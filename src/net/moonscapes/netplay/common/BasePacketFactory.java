package net.moonscapes.netplay.common;

import java.io.*;
import java.util.*;
import net.moonscapes.netplay.*;

/**
 */
public abstract class BasePacketFactory implements PacketFactory {
    protected PacketNameMapper nameMapper = new PacketNameMapper();


    /**
     */
    public Packet readPacket(DataInputStream in) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	int id = -1;
	String name = null;
	try {
	    id = in.readShort();
	    name = getPacketName(id);
	    if(name != null) {
		Class cl = Class.forName(name);
		Packet packet = (Packet)cl.newInstance();
		if(packet instanceof MetaPacket) {
		    MetaPacket mp = (MetaPacket)packet;
		    int n = in.readInt();
		    for(int i=0; i<n; i++) {
			Packet p = readPacket(in);
			mp.addPacket(p);
		    }
		} else {
		    packet.read(in);
		}
		return packet;
	    } else 
		throw new IOException("unknown packet id: "+ id);
	} catch(EOFException e) {
	    Log.debug("BasePacketFactory.readPacket(): "+ e);
	    //Log.debug("\tid: "+ id +", name: "+ name);
	    throw e;
	}
    }



    /**
     */
    public void writePacket(Packet packet, DataOutputStream out) throws IOException {
	if(packet instanceof MetaPacket) {
	    MetaPacket mp = (MetaPacket)packet;
	    out.writeShort(getPacketId(mp));
	    out.writeInt(mp.getPacketCount());
	    Enumeration e = mp.getPackets();
	    while(e.hasMoreElements())
		writePacket((Packet)e.nextElement(), out);
	} else {
	    out.writeShort(getPacketId(packet));
	    packet.write(out);
	}
    }

    /**
     */
    public PacketNameMapper getNameMapper() {
	return nameMapper;
    }

    /**
     */
    protected abstract int getPacketId(Packet packet);

    /**
     */
    protected abstract String getPacketName(int id);

}
