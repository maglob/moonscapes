package net.moonscapes.netplay.common;

import java.util.*;
import net.moonscapes.netplay.*;

/**
 * Handles the mappings between Packet class name and unique id, which
 * is used in transport protocol. This class just stores the information,
 * it is not the authority to assign new names or ids.
 *
 * @author Marko Aalto
 * @version $Id: PacketNameMapper.java,v 1.1 2001/10/24 18:04:38 mka Exp $
 */
public class PacketNameMapper {
    private Hashtable nameToId = new Hashtable();
    private Hashtable idToName = new Hashtable();

    /**
     */
    public PacketNameMapper() {
    }

    /**
     * @returns zero or positive integer on success, negative on if name
     *          is unknown;
     */
    public int getId(String name) {
	Integer i = (Integer)nameToId.get(name);
	return i!=null ? i.intValue() : -1;
    }

    /**
     */
    public String getName(int id) {
	return (String)idToName.get(new Integer(id));
    }

    /**
     */
    public synchronized void addName(String name, int id) {
	nameToId.put(name, new Integer(id));
	idToName.put(new Integer(id), name);
	Log.debug("packet name mapping added: "+ name +" "+ id);
    }
}
