package net.moonscapes.phys;

import java.util.*;

/**
 * Collection of MSObject.
 *
 * @author Marko Aalto
 * @version $Id: ObjectCollection.java,v 1.3 2001/11/10 10:38:20 mka Exp $
 * @see MSObject
 */
public class ObjectCollection {
    private Hashtable objects;

    /**
     */
    public ObjectCollection() {
	objects = new Hashtable();
    }


    /**
     */
    public void addObject(MSObject obj) {
	objects.put(new Integer(obj.getId()), obj);
    }


    /**
     */
    public MSObject getObject(int id) {
	return (MSObject)objects.get(new Integer(id));
    }

    /**
     */
    public Enumeration getObjects() {
	return objects.elements();
    }

    /**
     */
    public void removeObject(MSObject obj) {
	removeObject(obj.getId());
    }

    /**
     */
    public void removeObject(int id) {
	objects.remove(new Integer(id));
    }

    /**
     */
    public void removeAll() {
	objects.clear();

    }

    /**
     */
    public int getSize() {
	return objects.size();
    }
}
