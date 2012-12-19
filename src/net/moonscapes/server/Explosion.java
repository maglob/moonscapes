package net.moonscapes.server;

import net.moonscapes.phys.*;

/**
 */
class Explosion {
    private Vec2f pos;
    private float force;
    private int nspark, ndebris;
    private int objId;

    /**
     * @param objId zero mean no object assigned
     */
    Explosion(Vec2f pos, float force, int nspark, int ndebris, int objId) {
	this.pos = pos;
	this.force = force;
	this.nspark = nspark;
	this.ndebris = ndebris;
	this.objId = objId;
    }

    /**
     */
    Vec2f getPos() {
	return pos;
    }

    /**
     */
    float getForce() {
	return force;
    }

    /**
     */
    int getSparkCount() {
	return nspark;
    }

    /**
     */
    int getDebrisCount() {
	return ndebris;
    }

    /**
     */
    int getObjectId() {
	return objId;
    }

}
