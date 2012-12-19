package net.moonscapes.client;

import java.awt.*;
import net.moonscapes.phys.*;

/**
 * Wreck from object explosions. Purely client side object for visual effects.
 *
 * @author Marko Aalto
 * @version $Id: Wreck.java,v 1.1 2001/11/22 12:22:32 mka Exp $
 * @see Grob
 */
class Wreck extends Spark {
    private static final float FRICTION = 0.97f;
    private static final Vec2f G = new Vec2f(0, -.4f);
    //    protected Vec2f pos, vel;
    // protected int ttl;
    private float a, b;
    private float angle, rotSpeed;

    /**
     */
    Wreck(Vec2f pos, Vec2f vel, int ttl, float a, float b, float angle,
	  float rotSpeed) {
	super(pos, vel, ttl);
	this.a = a;
	this.b = b;
	this.angle = angle;
	this.rotSpeed = rotSpeed;
    }


    /**
     */
    public void update(Bitmap bm, int bmScale) {
	vel.add(G, vel);
	vel.mul(FRICTION, vel);
	pos.add(vel, pos);
	ttl--;
	if(bm.isSet((int)pos.x / bmScale, (int)pos.y / bmScale))
	    ttl = -1;
	angle += rotSpeed;
    }

    /**
     */
    public void paint(Graphics g) {
	Color c = Color.white;
	float ca = (float)Math.cos(angle);
	float sa = (float)Math.sin(angle);
	int ax = (int)(pos.x + a*ca);
	int ay = (int)(pos.y + a*sa);
	int bx = (int)(pos.x - b*ca);
	int by = (int)(pos.y - b*sa);
	g.drawLine(ax, -ay, bx, -by);
    }
}
