package net.moonscapes.netplay.server;

import java.io.*;
import net.moonscapes.netplay.*;
import net.moonscapes.netplay.common.*;


/**
 * Server side packet factory, which has the authority of issuing new
 * packet ids.
 *
 * @author Marko Aalto
 * @version $Id: ServerPacketFactory.java,v 1.1 2001/10/24 18:04:40 mka Exp $
 */
class ServerPacketFactory extends BasePacketFactory {
    private int nextPacketId = 1;    

    /**
     */
    protected int getPacketId(Packet packet) {
	String name = packet.getClass().getName();
	int id = nameMapper.getId(name);
	if(id < 0) {
	    synchronized(nameMapper) {
		id = nameMapper.getId(name);
		if(id < 0) {
		    id = nextPacketId++;
		    nameMapper.addName(name, id);
		}
	    }
	}
	return id;
    }

    /**
     */
    protected String getPacketName(int id) {
	return nameMapper.getName(id);
    }


}

