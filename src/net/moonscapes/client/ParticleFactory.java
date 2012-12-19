package net.moonscapes.client;

import java.awt.*;
import net.moonscapes.phys.Vec2f;
import net.moonscapes.phys.Bitmap;

/**
 * Factory and collection class for maintainig different groups
 * of particels.
 *
 * @author Marko Aalto
 * @version $Id: ParticleFactory.java,v 1.1 2001/11/22 12:22:32 mka Exp $
 * @see Spark
 * @see Debris
 */
class ParticleFactory {
    private Particle[] particles;
    private int nparticles;
    private Bitmap bitmap;
    private int bitmapScale;

    /**
     */
    ParticleFactory(int maxSize) {
	particles = new Particle[maxSize];
	nparticles = 0;
	this.bitmap = bitmap;
	this.bitmapScale = bitmapScale;
    }

    /**
     */
    void setBitmap(Bitmap bitmap, int bitmapScale) {
	this.bitmap = bitmap;
	this.bitmapScale = bitmapScale;
    }
    

    /**
     */
    int getSize() {
	return nparticles;
    }

    /**
     */
    void addParticle(Particle p) {
	if(nparticles < particles.length)
	    particles[nparticles++] = p;
    }

    /**
     */
    void update() {
	for(int i=0; i<nparticles; i++) {
	    Particle p = particles[i];
	    p.update(bitmap, bitmapScale);
	    if(p.isDead()) {
		particles[i] = particles[--nparticles];
		i--;
	    }
	}
    }


    /**
     */
    void paint(Graphics g) {
	for(int i=0; i<nparticles; i++)
	    particles[i].paint(g);
    }


    /**
     */
    void createSparks(Vec2f pos, Vec2f vel, double angle, double r, 
		      double spread, float force, int n, int ttl) {
	for(int i=0; i<n; i++)
	    if(nparticles < particles.length) {
		float f = force + (float)Math.random()*force;
		double a = spread/2 - Math.random()*spread;
		float ca = (float)Math.cos(angle + a);
		float sa = (float)Math.sin(angle + a);
		Vec2f p = pos.add(new Vec2f(ca*r, sa*r));
		Vec2f v = vel.add(new Vec2f(ca*f, sa*f));
		int t = ttl + (int)(Math.random()*ttl);
		particles[nparticles++] = new Spark(p, v, t);
	    }
    }


    /**
     */
    void createDebris(Vec2f pos, Vec2f vel, double angle, double r, 
		      double spread, float force, int n, int ttl) {
	for(int i=0; i<n; i++)
	    if(nparticles < particles.length) {
		float f = force + (float)Math.random()*force;
		double a = spread/2 - Math.random()*spread;
		float ca = (float)Math.cos(angle + a);
		float sa = (float)Math.sin(angle + a);
		Vec2f p = pos.add(new Vec2f(ca*r, sa*r));
		Vec2f v = vel.add(new Vec2f(ca*f, sa*f));
		int t = ttl + (int)(Math.random()*ttl);
		particles[nparticles++] = new Debris(p, v, t);
	    }
    }

    /**
     */
    void createWrecks(Vec2f pos, Vec2f vel, float force, Grob grob, int ttl) {
	if(grob == null)
	    return;

	Polygon p = grob.getPolygon();
	for(int i=0; i<p.npoints; i++) {
	    Vec2f a = new Vec2f(p.xpoints[i], p.ypoints[i]);
	    Vec2f b = new Vec2f(p.xpoints[(i+1) % p.npoints], 
				p.ypoints[(i+1) % p.npoints]);
	    float len = (float)a.sub(b).length();
	    if(nparticles < particles.length) {
		float f = force + (float)Math.random()*force;
		float lf = (float)Math.random();
		double ang = Math.random() * Math.PI*2;
		float ca = (float)Math.cos(ang);
		float sa = (float)Math.sin(ang);
		Vec2f v = vel.add(new Vec2f(ca*f, sa*f));
		Vec2f pp = pos.add(a.add(b).mul(0.5f));
		int t = ttl + (int)(Math.random()*ttl);
		float rs = 0.05f + (float)Math.random() * 0.7f;
		particles[nparticles++] = new Wreck(pp, v, t, len*lf, 
						    len*(1f-lf), 0, rs);
	    }
	}
    }

}

