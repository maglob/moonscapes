package net.moonscapes.server;

import net.moonscapes.phys.*;

/**
 */
class Ship extends ServerObject {
    private static final long REFIRE_TIME = 300;
    private float thrustForce;
    private float turnSpeed;
    private float ammoSpeed;
    private long lastFireTime;
    private float energy, maxEnergy;
    private Player player;
    private boolean isShieldOn, isThrustOn;

    /**
     */
    Ship(Player player) {
	this.player = player;
	thrustForce = 0.3f;
	turnSpeed = 0.15f;
	ammoSpeed = 8;
	maxEnergy = 100;
	energy = maxEnergy;
    }

    /**
     */
    String getModelName() {
	return "ship";
    }

    /**
     */
    void fire(Shot shot) {
	if(lastFireTime+REFIRE_TIME < System.currentTimeMillis()) {
	    float ca = (float)Math.cos(getDir());
	    float sa = (float)Math.sin(getDir());
	    Vec2f v = new Vec2f(ca*ammoSpeed, sa*ammoSpeed);
	    float r = 1 + getModel().getRadius();
	    shot.setPos(getPos().add(new Vec2f(ca*r, sa*r)));
	    v.add(getVelocity(), v);
	    shot.setVelocity(v);
	    lastFireTime = System.currentTimeMillis();
	}
    }

    /**
     */
    void turnLeft() {
	rotate(turnSpeed);
    }

    /**
     */
    void turnRight() {
	rotate(-turnSpeed);
    }

    /**
     */
    void thrust() {
	Vec2f v = getVelocity();
	v.x += (float)Math.cos(getDir()) * thrustForce;
	v.y += (float)Math.sin(getDir()) * thrustForce;
    }

    /**
     */
    float getEnergy() {
	return energy;
    }

    /**
     */
    float getMaxEnergy() {
	return maxEnergy;
    }

    /**
     */
    Player getPlayer() {
	return player;
    }

    /**
     */
    void setEnergy(float energy) {
	this.energy = energy;
    }

    /**
     */
    void addEnergy(float delta) {
	energy += delta;
    }

    /**
     */
    void setShieldOn(boolean b) {
	isShieldOn = b;
	if(energy <= 0)
	    isShieldOn = false;

    }

    /**
     */
    boolean isShieldOn() {
	return isShieldOn;
    }

    /**
     */
    void setThrustOn(boolean b) {
	isThrustOn = b;
    }

    /**
     */
    boolean isThrustOn() {
	return isThrustOn;
    }

    /**
     */
    void update() {
	super.update();
	if(isShieldOn) {
	    energy -= 0.5f;
	    if(energy < 0)
		energy = 0;
	}
    }
}
