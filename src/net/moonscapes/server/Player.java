package net.moonscapes.server;

import net.moonscapes.netplay.*;
import net.moonscapes.phys.*;

/**
 * Player connected to Moonscapes server.
 *
 * @author Marko Aalto
 * @version $Id: Player.java,v 1.5 2001/11/22 12:16:30 mka Exp $
 */
class Player {
    public static final int
	STATE_WAIT  = 1,
	STATE_FLY   = 2;
    private int state;
    private Client client;
    private Ship ship;
    private Console console;
    private String name;
    private int score;
    private long waitTime;
    private Vec2f focus;

    
    /**
     */
    Player(Client client, String name) {
	this.client = client;
	this.name = name;
	score = 0;
	console = new Console();
	state = STATE_FLY;
    }

    /**
     */
    Client getClient() {
	return client;
    }

    /**
     */
    void setShip(Ship ship) {
	this.ship = ship;
    }

    /**
     */
    Ship getShip() {
	return ship;
    }

    /**
     */
    String getName() {
	return name;
    }

    /**
     */
    int getScore() {
	return score;
    }

    /**
     */
    void addScore(int points) {
	score += points;
    }

    /**
     */
    Console getConsole() {
	return console;
    }

    /**
     */
    void setWaitTime(long time) {
	waitTime = time;
    }

    /**
     */
    long getWaitTime() {
	return waitTime;
    }

    /**
     */
    int getState() {
	return state;
    }

    /**
     */
    void setState(int state) {
	this.state = state;
    }

    /**
     */
    void setFocus(Vec2f focus) {
	this.focus = focus;
    }

    /**
     */
    Vec2f getFocus() {
	return focus;
    }
    
}
