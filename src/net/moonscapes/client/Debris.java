package net.moonscapes.client;

import java.awt.*;
import net.moonscapes.phys.*;

/**
 * Debris from explosions, affected by gravity and bounces.
 *
 * @author Marko Aalto
 * @version $Id: Debris.java,v 1.1 2001/11/22 12:22:32 mka Exp $
 */
class Debris extends Spark {
    private static final Vec2f G = new Vec2f(0, -.4f);
    private static final float FRICTION = 0.97f;

    /**
     */
    Debris(Vec2f pos, Vec2f vel, int ttl) {
	super(pos, vel, ttl);
    }

    /**
     */
    public void update(Bitmap bm, int bmScale) {
	vel.add(G, vel);
	vel.mul(FRICTION, vel);
	pos.add(vel, pos);
	ttl--;

	if(bm.isSet((int)pos.x / bmScale, (int)pos.y / bmScale)) {
	    vel.y = -vel.y * 0.7f;
	    pos.y += vel.y;
	}
	if(bm.isSet((int)pos.x / bmScale, (int)pos.y / bmScale)) 
	    ttl = -1;
    }

    /**
     */
    public void paint(Graphics g) {
	g.setColor(Color.white);
	g.drawRect((int)pos.x, -(int)pos.y, 0, 0);
    }
}
