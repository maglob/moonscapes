package net.moonscapes.server;

import java.util.*;
import net.moonscapes.phys.*;

/**
 * Cave where players can fly and objects exists.
 *
 * @author Marko Aalto
 * @version $Id: Cave.java,v 1.14 2001/11/22 12:16:30 mka Exp $
 */
class Cave {
    private ObjectCollection objects;
    private Vec2f gravity;
    private float friction;
    private Bitmap bitmap;
    private Vector shots;
    private long shotLiveTime;
    private int bitmapScale;
    private Vec2f[] shipInitPositions = new Vec2f[] {
	new Vec2f(300, 300),
	new Vec2f(1700, 300),
	new Vec2f(300, 1800),
	new Vec2f(1850, 1850),
	new Vec2f(1025, 1300)
    };
    private Vector explosions;


    /**
     */
     Cave() {
	 objects = new ObjectCollection();
	 shots = new Vector();
	 gravity = new Vec2f(0, -0.04f);
	 friction = 0.99f;
	 shotLiveTime = 4000;
	 explosions = new Vector();

	 int w = 512;
	 int h = 512;
	 bitmap = new Bitmap(w, h);
	 bitmapScale = 4;	 
	 int border = 32;
	 bitmap.setCircle(w/2, h/2, 64*2, true);
	 bitmap.setRect(0, 0, border, h);
	 bitmap.setRect(w-border, 0, border, h);
	 bitmap.setRect(0, h-border, w, border);
	 bitmap.setRect(0, 0, w, border);
	 bitmap.setRect(border*3, h/4, w/3, border);
	 bitmap.setCircle(w-100, h-100, 50, true);
	 bitmap.setRect(w/2-20, border, 40, 50);
	 bitmap.setRect(w/2+20, border+40, 100, 10);
	 bitmap.clearRect(w/2-15, h/2, 30, 150);
	 bitmap.clearRect(w/2, h/2-15, 150, 30);
     }


    /**
     */
    Enumeration getObjects() {
	return objects.getObjects();
    }

    /**
     */
    Enumeration getShots() {
	return shots.elements();
    }

    /**
     */
    Enumeration getExplosions() {
	return explosions.elements();
    }


    /**
     */
    void clearExplosions() {
	explosions.clear();
    }


    /**
     */
    void addObject(MSObject o) {
	if(o instanceof Shot)
	    shots.addElement(o);
	else
	    objects.addObject(o);
    }

    /**
     */
    void removeObject(MSObject o) {
	if(o instanceof Shot)
	    shots.removeElement(o);
	else
	    objects.removeObject(o);
	
    }

    /**
     */
    Bitmap getBitmap() {
	return bitmap;
    }

    /**
     */
    int getBitmapScale() {
	return bitmapScale;
    }


    /**
     */
    boolean isHit(Vec2f v) {
	int x = (int)(v.x+.5f) / bitmapScale;
	int y = (int)(v.y+.5f) / bitmapScale;
	return bitmap.isSet(x, y);
    }

    /**
     */
    MSObject getObject(int id) {
	return objects.getObject(id);
    }


    /**
     */
    Vec2f getShipInitPosition() {
	int n = (int)(shipInitPositions.length * Math.random());
	return new Vec2f(shipInitPositions[n]);
    }


    /**
     */
    private void shipExplosion(Ship s) {
	Explosion exp = new Explosion(s.getPos(), 10, 30, 20, s.getId());
	explosions.addElement(exp);
    }


    /**
     * Update the simulation for one frame.
     */
    void update() {
	// Update objects
	Enumeration e = getObjects();
	while(e.hasMoreElements()) {
	    ServerObject o = (ServerObject)e.nextElement();	    
	    if(o instanceof Ship) {
		Ship s = (Ship)o;
		Vec2f v = s.getVelocity();
		v.add(gravity, v);
		v.mul(friction, v);
		Vec2f[] ver = s.getModel().getVertices(s.getDir());
		Vec2f temp = new Vec2f();
		for(int i=0; i<ver.length; i++) {
		    temp.x = s.getPos().x + ver[i].x;
		    temp.y = s.getPos().y + ver[i].y;
		    if(isHit(temp)) {
			s.addEnergy(-200);
			Player plr = s.getPlayer();
			plr.addScore(-1);		
			shipExplosion(s);
			break;			
		    }
		}
	    }
	    o.update();
	}

	// Update shots
	long time = System.currentTimeMillis();
	for(int i=0; i<shots.size(); i++) {
	    Shot s = (Shot)shots.elementAt(i);
	    if(s.getCreateTime() + shotLiveTime < time) {
		shots.removeElementAt(i--);
	    } else {
		s.update();
		if(isHit(s.getPos())) {
		    shots.removeElementAt(i--);
		}
	    }
	}

	// Check shot-object collisions
	e = getObjects();
	while(e.hasMoreElements()) {
	    ServerObject o = (ServerObject)e.nextElement();
	    Polygon poly = o.getModel().getPolygon(o.getDir());
	    for(int i=0; i<shots.size(); i++) {
		Shot s = (Shot)shots.elementAt(i);
		Vec2f v = s.getPos().sub(o.getPos());
		if(poly.hasPoint(v)) {
		    shots.removeElementAt(i--);		    
		    if(o instanceof Ship) {
			Ship t = (Ship)o;
			if(!t.isShieldOn())
			    t.addEnergy(-40);
			if(t.getEnergy()<0 && s.getParent() instanceof Ship) {
			    Player plr = ((Ship)s.getParent()).getPlayer();
			    plr.addScore(1);
			    shipExplosion(t);
			}
		    }
		}
	    }		
	}

    }
}



