package net.moonscapes.client;

import java.awt.Graphics;
import net.moonscapes.phys.*;

/**
 * Graphical object. Used in client side rendering of Models and
 * MSObjects.
 *
 * @author Marko Aalto
 * @version $Id: Grob.java,v 1.10 2001/11/22 12:16:28 mka Exp $
 * @see net.moonscapes.phys.Model
 * @see net.moonscapes.phys.MSObject
 */
class Grob {
    private Model model;
    private java.awt.Polygon[] polygons;
    private int dirs;
    private float halfStep;

    /**
     */
    public Grob(Model model) {
	this.model = model;
	this.dirs = model.getDirs();
	halfStep = (float)(Math.PI*2 / dirs / 2);
	polygons = new java.awt.Polygon[dirs];
	int n = model.getVertices().length;
	int xp[] = new int[n];
	int yp[] = new int[n];
	for(int j=0; j<polygons.length; j++) {
	    Vec2f[] v = model.getVertices((float)Math.PI*2*j/dirs);
	    for(int i=0; i<n; i++) {
		xp[i] = (int)(v[i].x + 0.5f);
		yp[i] = -(int)(v[i].y + 0.5f);
	    }
	    polygons[j] = new java.awt.Polygon(xp, yp, n);
		
	}
    }


    /**
     */
    public void paint(Graphics g, float angle) {
	float hs = angle<0 ? -halfStep : halfStep;
	int n = (int)(dirs * (angle+hs) / (float)(Math.PI*2)) % dirs;
	if(n < 0)
	    n += dirs;
	g.drawPolygon(polygons[n]);
    }


    /**
     */
    float getRadius() {
	return model.getRadius();
    }

    /**
     */
    java.awt.Polygon getPolygon() {
	return polygons[0];
    }
}









