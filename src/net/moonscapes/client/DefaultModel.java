package net.moonscapes.client;

import java.util.*;
import net.moonscapes.phys.*;

/**
 * Default model for objects whose model is not know.
 *
 * @author Marko Aalto
 * @version $Id: DefaultModel.java,v 1.5 2001/11/08 00:25:22 mka Exp $
 * @see net.moonscapes.phys.Model
 */
class DefaultModel extends Model {
    private static Vec2f[] vertices;

    static {
	Vector v = new Vector();
	v.addElement(new Vec2f(0, 0));
	v.addElement(new Vec2f(10, 0));
	v.addElement(new Vec2f(10, 20));
	vertices = new Vec2f[v.size()];
	for(int i=0; i<vertices.length; i++)
	    vertices[i] = (Vec2f)v.elementAt(i);
    }

    /**
     */
    DefaultModel() {
	super("default", vertices, Model.DEFAULT_DIRS);
    }
}
