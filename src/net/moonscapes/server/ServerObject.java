package net.moonscapes.server;

import net.moonscapes.phys.*;

/**
 * Server side object. Used for storing extra model data used by
 * the server.
 *
 * @author Marko Aalto
 * @version $Id: ServerObject.java,v 1.7 2001/11/08 00:25:30 mka Exp $
 * @see net.moonscapes.phys.MSObject
 */
class ServerObject extends MSObject {
    private static int nextObjectId = 1;
    private Model model;
    private Vec2f velocity;

    /**
     */
    ServerObject() {
	super(getNextObjectId());
	this.velocity = new Vec2f(0, 0);
    }

    /**
     */
    Model getModel() {
	if(model == null)
	    model = ModelFactory.getModelFactory().getModel(this);
	return model;
    }    

    /**
     */
    void update() {
	move(velocity);
    }

    /**
     */
    Vec2f getVelocity() {
	return velocity;
    }

    /**
     */
    void setVelocity(Vec2f velocity) {
	this.velocity = velocity;
    }

    /**
     */
    private static synchronized int getNextObjectId() {
	return nextObjectId++;
    }
}

