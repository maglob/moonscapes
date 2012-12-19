package net.moonscapes.phys;

/**
 * Polygon, collection of vertices.
 *
 * @author Marko Aalto
 * @version $Id: Polygon.java,v 1.2 2001/11/02 21:08:38 mka Exp $
 * @see Vec2f
 */
public class Polygon {
    private Vec2f[] vert;
    private float radius;

    /**
     */
    private Polygon() {
    }

    /**
     */
    public Polygon(Vec2f[] vertices) {
	this.vert = vertices;
	calcRadius();
    }


    /**
     */
     public Polygon(Polygon poly) {
	 vert = new Vec2f[poly.vert.length];
	 for(int i=0; i<vert.length; i++)
	     vert[i] = new Vec2f(poly.vert[i]);
	 calcRadius();
     }


    /**
     */
    Vec2f[] getVertices() {
	return vert;
    }

    /**
     */
    public void rotate(float a) {
	for(int i=0; i<vert.length; i++) {
	    float x = vert[i].x;
	    float y = vert[i].y;
	    vert[i].x = (float)(Math.cos(a)*x + Math.sin(a)*y);
	    vert[i].y = (float)(Math.sin(a)*x - Math.cos(a)*y);
	}
    }


    /**
     * @return true if point p is inside the polygon
     */
    public boolean hasPoint(Vec2f p) {
	if(p.lengthSquared() > radius*radius)
	    return false;

	int hits = 0;

	int npoints = vert.length;

        float lastx = vert[npoints - 1].x;
        float lasty = vert[npoints - 1].y;
        float curx, cury;
	float x = p.x;
	float y = p.y;

        // Walk the edges of the polygon
        for (int i=0; i<npoints; lastx=curx, lasty=cury, i++) {
            curx = vert[i].x;
            cury = vert[i].y;

            if (cury == lasty) 
                continue;

            float leftx;
            if (curx < lastx) {
                if (x >= lastx) 
                    continue;
                leftx = curx;
            } else {
                if (x >= curx) 
                    continue;
                leftx = lastx;
            }

            float test1, test2;
            if (cury < lasty) {
                if (y < cury || y >= lasty) 
                    continue;
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            } else {
                if (y < lasty || y >= cury)
                    continue;
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }
            if (test1 < (test2 / (lasty - cury) * (lastx - curx)))
                hits++;
        }
        return (hits & 1) != 0;
    }


    /**
     */
    private void calcRadius() {
	for(int i=0; i<vert.length; i++) {
	    Vec2f v = vert[i];
	    float r = v.x*v.x + v.y*v.y;
	    if(r > radius) 
		radius = r;
	}
	radius = (float)Math.sqrt(radius);
    }

    /**
     */
    public float getRadius() {
	return radius;
    }


    /**
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();
	for(int i=0; i<vert.length; i++) {
	    sb.append(vert[i].toString());
	}
	return sb.toString();
    }
}
