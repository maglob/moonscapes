package net.moonscapes.phys;

/**
 * Base class for all objects in Moonscapes universe.
 *
 * @author Marko Aalto
 * @version $Id: MSObject.java,v 1.3 2001/11/08 00:25:27 mka Exp $
 */
public class MSObject {
    private int id;
    private Vec2f pos;
    private float dir;

    /**
     * Dont allow null constructors (undefined id)
     */
    private MSObject() {
    }

    /**
     */
    public MSObject(int id) {
	this.id = id;
	this.pos = new Vec2f(0, 0);
    }

    
    /**
     */
    public int getId() {
	return id;
    }

    /**
     */
    public Vec2f getPos() {
	return pos;
    }

    /**
     */
    public void setPos(Vec2f pos) {
	this.pos = pos;
    }

    /**
     */
    public void move(Vec2f delta) {
	pos.add(delta, pos);
    }

    /**
     */
    public float getDir() {
	return dir;
    }

    /**
     */
    public void setDir(float dir) {
	this.dir = dir;
    }

    /**
     */
    public void rotate(float delta) {
	dir += delta;
    }
}
