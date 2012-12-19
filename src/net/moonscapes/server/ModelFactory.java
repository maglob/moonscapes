package net.moonscapes.server;

import java.io.*;
import java.util.*;
import net.moonscapes.phys.*;
import net.moonscapes.netplay.Log;

/**
 * Utility class for creating Models.
 *
 * @author Marko Aalto
 * @version $Id: ModelFactory.java,v 1.4 2001/11/07 20:14:47 mka Exp $
 * @see Model
 */
public class ModelFactory {
    private static ModelFactory modelFactory = new ModelFactory();
    private Hashtable models;
    private Hashtable modelNames;


    /**
     * Unit testing
     */
    public static void main(String[] args) {
	ModelFactory mf = ModelFactory.getModelFactory();
	Model m = mf.getModel("ship");
	System.out.println("m1: "+ m);
	m = mf.getModel("ship");
	System.out.println("m2: "+ m);
    }

    /**
     */
    private ModelFactory() {
	models = new Hashtable();
	modelNames = new Hashtable();
	modelNames.put((new Shot()).getClass().getName(), "shot");
	modelNames.put((new Ship(null)).getClass().getName(), "ship");
	modelNames.put((new Box()).getClass().getName(), "box");
    }


    /**
     */
    public static ModelFactory getModelFactory() {
	return modelFactory;
    }


    /**
     * Creates model from strings in format "(x,y)(x,y)(x,y)"
     */
    public Model createModel(String name, String str) {
	Vector v = new Vector();
	StringTokenizer st = new StringTokenizer(str, "() ");
	while(st.hasMoreTokens()) {
	    String s = st.nextToken();
	    int comma = s.indexOf(',');
	    float a = Float.parseFloat(s.substring(0, comma));
	    float b = Float.parseFloat(s.substring(comma + 1));
	    v.addElement(new Vec2f(a, b));
	}
	return new Model(name, (Vec2f[])v.toArray(new Vec2f[0]));
    }


    /**
     * Get a named model.
     */
    public Model getModel(String modelName) {
	Model m = (Model)models.get(modelName);
	if(m == null) {
	    m = loadModel(modelName);
	    models.put(modelName, m);
	}
	return m;
    }

    /**
     * Get a model for class of ServerObject
     */
    public Model getModel(ServerObject o) {
	String name = (String)modelNames.get(o.getClass().getName());
	if(name == null) {
	    Log.error("ModelFactory.getModel(): no model name for object: "+o);
	    System.exit(0);
	    return null;
	} else	
	    return getModel(name);	
    }

    /**
     */
    private Model loadModel(String modelName) {
	ClassLoader cl = getClass().getClassLoader();
	String base = "net/moonscapes/server/model/";
	InputStream is = cl.getResourceAsStream(base + modelName +".mdl");
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	try {
	    return createModel(modelName, br.readLine());
	} catch(IOException e) {
	    Log.error("PacketFactory.loadModel(): "+ e);
	    return null;
	} finally {
	    try { br.close(); } catch(IOException e) {}
	    try { is.close(); } catch(IOException e) {}
	}

    }
}
