package net.moonscapes.client;

import java.awt.*;
import net.moonscapes.phys.*;

/**
 * Optimized version of Bitmap for speeding up the client side
 * rendering process, by representing the bitmap as group of
 * horizontal lines.
 *
 * @author Marko Aalto
 * @verison $Id: LineBitmap.java,v 1.4 2001/11/04 18:55:36 mka Exp $
 * @see net.moonscapes.phys.Bitmap
 */
class LineBitmap {
    private short[][] slices;
    private int scale;
    
    /**
     */
    LineBitmap(Bitmap bm, int scale) {
	this.scale = scale;
	slices = new short[bm.getHeight()][];	
	short[] temp = new short[bm.getWidth() + 1];	
	for(int y=0; y<bm.getHeight(); y++) {
	    int w = bm.getWidth();
	    int n = 0;
	    short run = 0;
	    boolean isSet = true;
	    for(int x=0; x<w; x++) {
		if(bm.isSet(x, y)) {
		    if(isSet)
			run++;
		    else {
			temp[n++] = run;
			isSet = true;
			run = 1;
		    }
		} else {
		    if(isSet) {
			temp[n++] = run;
			isSet = false;
			run = 1;
		    } else 
			run++;
		}
	    }
	    if(run > 0)
		temp[n++] = run;
	    slices[y] = new short[n];
	    System.arraycopy(temp, 0, slices[y], 0, n);	    
	}
    }

    /**
     */
    void paint(Graphics g) {
	for(int y=0; y<slices.length; y++) {
	    boolean isDraw = true;
	    int n = 0;
	    short[] line = slices[y];
	    for(int x=0; x<line.length; x++) {
		if(isDraw) {
		    //g.drawLine(n*scale, -y*scale, (n+line[x])*scale, -y*scale);
		    g.drawRect(n*scale, -y*scale-scale, line[x]*scale, scale);
		    //g.fillRect(n*scale, -y*scale-scale, line[x]*scale, scale);

		}
		n += line[x];
		isDraw = !isDraw;
	    }
	}
	
    }

    /**
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();
	for(int y=0; y<slices.length; y++) {
	    sb.append(y);
	    sb.append(": ");
	    for(int x=0; x<slices[y].length; x++) {
		if(x != 0)
		    sb.append(',');
		sb.append(slices[y][x]);
	    }
	    sb.append('\n');
	}
	return sb.toString();
    }
}
