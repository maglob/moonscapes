package net.moonscapes.phys;

/**
 * Physical model used in MSObjects.
 *
 * @author Marko Aalto
 * @version $Id: Model.java,v 1.6 2001/11/08 00:25:27 mka Exp $
 */
public class Model {
    public static final int DEFAULT_DIRS = 32;
    private Polygon[] polygons;
    private float halfStep;
    private String name;

    /**
     */
    public Model() {
    }

    /**
     */
    public Model(String name, Vec2f[] vertices) {
	this(name, vertices, DEFAULT_DIRS);
    }


    /**
     */
    public Model(String name, Vec2f[] vert, int dirs) {
	this.name = name;
	halfStep = (float)(Math.PI*2 / dirs / 2);	

	polygons = new Polygon[dirs];	
	polygons[0] = new Polygon(vert);
	for(int i=1; i<polygons.length; i++) {
	    polygons[i] = new Polygon(polygons[0]);
	    polygons[i].rotate((float)Math.PI*2*i / polygons.length); 
	}

    }


    /**
     */
    public Vec2f[] getVertices() {
	return getVertices(0);
    }


    /**
     */
    public Vec2f[] getVertices(float angle) {
	return getPolygon(angle).getVertices();
    }

    /**
     */
    public Polygon getPolygon(float angle) {
	float hs = angle<0 ? -halfStep : halfStep;
	int n = (int)(polygons.length * (angle+hs) / (float)(Math.PI*2)) % 
	    polygons.length;
	if(n < 0)
	    n += polygons.length;
	return polygons[n];
    }

        
    /**
     */
    public int getDirs() {
	return polygons.length;
    }


    /**
     */
    public float getRadius() {
	return polygons[0].getRadius();
    }

    /**
     */
    public String getName() {
	return name;
    }
}
