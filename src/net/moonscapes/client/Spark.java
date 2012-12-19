package net.moonscapes.client;

import java.awt.*;
import net.moonscapes.phys.*;

/**
 * Spark from thrusts, fireballs and exploisions. Purely client side
 * object for visual effects.
 *
 * @author Marko Aalto
 * @version $Id: Spark.java,v 1.1 2001/11/22 12:22:32 mka Exp $
 */
class Spark implements Particle {
    private static final float FRICTION = 0.9f;
    protected Vec2f pos, vel;
    protected int ttl;

    /**
     */
    Spark(Vec2f pos, Vec2f vel, int ttl) {
	this.pos = pos;
	this.vel = vel;
	this.ttl = ttl;
    }


    /**
     */
    public void update(Bitmap bm, int bmScale) {
	vel.mul(FRICTION, vel);
	pos.add(vel, pos);
	ttl--;
	if(bm.isSet((int)pos.x / bmScale, (int)pos.y / bmScale))
	    ttl = -1;
    }

    /**
     */
    public boolean isDead() {
	return ttl < 0;
    }



    /**
     */
    public void paint(Graphics g) {
	Color c = Color.white;
	if(ttl < 8)
	    c = Color.lightGray;
	if(ttl < 4)
	    c = Color.gray;
	g.setColor(c);
	//g.drawRect((int)pos.x, -(int)pos.y, 0, 0);
	g.drawLine((int)pos.x, -(int)pos.y, (int)pos.x, -(int)pos.y);
    }
}
