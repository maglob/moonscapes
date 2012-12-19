package net.moonscapes.client;

import net.moonscapes.phys.*;

/**
 * Client side representation of MSObject. Contains some
 * extra data for helping client side rendering.
 *
 * @author Marko Aalto
 * @version $Id: ClientObject.java,v 1.7 2001/11/21 08:31:34 mka Exp $
 * @see net.moonscapes.phys.MSObject
 */
class ClientObject extends MSObject {
    private Grob grob;
    private String title;
    private boolean isShieldOn, isThrustOn;
    private int energy, maxEnergy;
    private boolean isVisible;
    private Vec2f vel;


    /**
     */
    ClientObject(int id) {
	super(id);
	isVisible = true;
    }

    /**
     */
    Grob getGrob() {
	return grob;
    }

    /**
     */
    void setGrob(Grob grob) {
	this.grob = grob;
    }

    /**
     */
    void setTitle(String title) {
	this.title = title;
    }

    /**
     */
    String getTitle() {
	return title;
    }

    /**
     */
    boolean isShieldOn() {
	return isShieldOn;
    }

    /**
     */
    void setShieldOn(boolean b) {
	isShieldOn = b;
    }

    /**
     */
    int getEnergy() {
	return energy;
    }

    /**
     */
    void setEnergy(int e) {
	energy = e;
    }

    /**
     */
    int getMaxEnergy() {
	return maxEnergy;
    }

    /**
     */
    void setMaxEnergy(int e) {
	maxEnergy = e;
    }

    /**
     */
    boolean isVisible() {
	return isVisible;
    }

    /**
     */
    void setVisible(boolean b) {
	isVisible = b;
    }

    /**
     */
    boolean isThrustOn() {
	return isThrustOn;
    }

    /**
     */
    void setThrustOn(boolean b) {
	isThrustOn = b;
    }

    /**
     */
    void setVelocity(Vec2f vel) {
	this.vel = vel;
    }

    /**
     */
    Vec2f getVelocity() {
	return vel;
    }
}
