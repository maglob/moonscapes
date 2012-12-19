package net.moonscapes.server;

import java.util.*;
import net.moonscapes.netplay.*;
import net.moonscapes.packet.*;
import net.moonscapes.phys.*;

/**
 * Moonscapes Server. The initilization and main logic of the game
 * is here.
 *
 * @author Marko Aalto
 * @version $Id: Server.java,v 1.24 2001/11/22 12:16:30 mka Exp $
 */
public final class Server implements ServerHandler, Runnable {
    private NetplayServer server;
    private Hashtable players;
    private volatile boolean isRunning;
    private int frameCount = 0;
    private Cave cave;
    private long frames;

    /**
     */
    public static void main(String[] args) {
	int port = 9009;
	try {
	    Server s = new Server(port);	
	} catch(NetplayException e) {
	    System.err.println("Server.main(): "+ e);
	}
    }


    /**
     */
    private Server(int port) throws NetplayException {
	server = Netplay.createServer(port, this);
	players = new Hashtable();
	cave = new Cave();	
	Box b = new Box();
	b.setPos(new Vec2f(200, 200));
	cave.addObject(b);
	Thread t = new Thread(this, "main");
	isRunning = true;
	t.start();
	server.start();
    }

    /**
     */
    public void run() {
	int delay = 1000 / 20;
	while(isRunning) {
	    try {
		Thread.sleep(delay);
		update();
		frames++;
	    } catch(InterruptedException e) {
		Log.error("Server.run(): "+ e);
	    }
	    
	}
    }

    /**
     * Updates game world one frame.
     */
    private void update() {
	frameCount++;
	updatePlayerConsoles();
	cave.update();
	checkPlayerDeaths();
	sendPackets();       	
    }
    
    
    /**
     */
    private void initPlayerShip(Player plr) {
	Ship s = plr.getShip();
	if(s != null) {
	    s.setPos(cave.getShipInitPosition());
	    s.setEnergy(100);
	    s.setVelocity(new Vec2f(0, 0));
	    s.setDir((float)Math.PI/2);
	    cave.addObject(s);
	}
    }

    /**
     */
    private void checkPlayerDeaths() {
	long time = System.currentTimeMillis();

	for(Enumeration e=players.elements(); e.hasMoreElements(); ) {
	    Player plr = (Player)e.nextElement();
	    Ship s = plr.getShip();
	    switch(plr.getState()) {
	    case Player.STATE_WAIT:
		if(time > plr.getWaitTime()) {
		    plr.setState(Player.STATE_FLY);
		    initPlayerShip(plr);
		}
		break;
	    case Player.STATE_FLY:
		if(s!=null && s.getEnergy()<0) {
		    plr.setFocus(new Vec2f(s.getPos()));
		    initPlayerShip(plr);
		    plr.setState(Player.STATE_WAIT);
		    plr.setWaitTime(time + 3000);
		    cave.removeObject(plr.getShip());		
		} else
		    plr.setFocus(s.getPos());
		break;
	    }
	}
    }


    /**
     */
    private void updatePlayerConsoles() {
	for(Enumeration e=players.elements(); e.hasMoreElements(); ) {
	    Player plr = (Player)e.nextElement();
	    Ship s = plr.getShip();
	    if(s != null) {
		Console c = plr.getConsole();
		if(c.getKey(Console.LEFT))
		    s.turnLeft();
		if(c.getKey(Console.RIGHT))
		    s.turnRight();
		if(c.getKey(Console.FIRE) && !s.isShieldOn()) {
		    Shot shot = new Shot(s);
		    s.fire(shot);
		    cave.addObject(shot);
		}
		if(c.getKey(Console.THRUST))
		    s.thrust();
		s.setShieldOn(c.getKey(Console.SHIELD));
		s.setThrustOn(c.getKey(Console.THRUST));
	    }
	}
    }

    /**
     */
    private void sendPackets() {	
	// Build up shots update packet, to be sent to all players
	Vector v = new Vector();
	for(Enumeration e=cave.getShots(); e.hasMoreElements(); ) 
	    v.addElement(((MSObject)e.nextElement()).getPos());
	UpdateShotsPacket usp = new UpdateShotsPacket(v);

	// Build up objects update packet, to be sent to all players
	Vector v2 = new Vector();
	for(Enumeration e=cave.getObjects(); e.hasMoreElements(); ) 
	    v2.addElement(e.nextElement());
	UpdateObjectsPacket uop = new UpdateObjectsPacket(v2);

	// Build up score sheet object
	Vector v3 = new Vector();
	Vector shipAttr = new Vector();
	for(Enumeration ep=players.elements(); ep.hasMoreElements(); ) {
	    Player p = (Player)ep.nextElement();	    
	    v3.addElement(new ScoreEntry(p.getName(), p.getScore()));
	    Ship s = p.getShip();
	    Packet sap = new ShipAttributesPacket(s, s.isShieldOn(), 
						  s.getEnergy(), 
						  s.getMaxEnergy(),
						  s.isThrustOn(), 
						  s.getVelocity());
						  
	    shipAttr.addElement(sap);
	}
	class Comp implements Comparator {
	    public int compare(Object a, Object b) {
		return ((ScoreEntry)b).getScore() - ((ScoreEntry)a).getScore();
	    }
	}
	Collections.sort(v3, new Comp());
	ScoreSheetPacket ssp = new ScoreSheetPacket(v3);

	// Temporary/multiused focus packet
	Vec2f focus = new Vec2f();
	FocusPacket fp = new FocusPacket(focus);

	// Build up meta packet
	MetaPacket mp = new MetaPacket();
	mp.addPacket(uop);
	mp.addPacket(usp);
	mp.addPacket(ssp);
	for(int i=0; i<shipAttr.size(); i++)
	    mp.addPacket((Packet)shipAttr.elementAt(i));
	mp.addPacket(fp);
	mp.addPacket(new FramePacket(frames, System.currentTimeMillis()));

	// Add explosions
	for(Enumeration e=cave.getExplosions(); e.hasMoreElements(); ) {
	    Explosion exp = (Explosion)e.nextElement();
	    mp.addPacket(new ExplosionPacket(exp.getPos(), exp.getForce(),
					     exp.getSparkCount(),
					     exp.getDebrisCount(),
					     exp.getObjectId() ));
	}
	cave.clearExplosions();

	for(Enumeration ep=players.elements(); ep.hasMoreElements(); ) {
	    Player plr = (Player)ep.nextElement();
	    Client client = plr.getClient();
	    Vec2f pos = plr.getFocus();
	    focus.set((int)(pos.x+.5), (int)(pos.y+.5));
	    try {
		if(client.isConnected()) 
		    server.sendPacket(mp, client);
	    } catch(NetplayException ex) {
		Log.error("Server.sendPackets(): client: "+ client +
			  " :"+ ex);
		client.disconnect();
	    }
	}
    }



    /**
     */
    public void clientConnect(Client client) {
	Log.log("client "+ client.getId() +": connected");
    }


    /**
     */
    public void clientDisconnect(Client client) {
	Player plr = (Player)players.get(client);
	players.remove(client);
	cave.removeObject(plr.getShip());
	Log.log("client "+ client.getId() +": disconnect");
    }


    /**
     */
    public void handlePacket(Packet packet, Client source) {
	try {
	    if(packet instanceof LoginPacket)
		login((LoginPacket)packet, source);
	    else if(packet instanceof ObjectInfoRequestPacket)
		objectInfoRequest((ObjectInfoRequestPacket)packet, source);
	    else if(packet instanceof ModelRequestPacket)
		modelRequest((ModelRequestPacket)packet, source);
	    else if(packet instanceof ConsolePacket)
		console((ConsolePacket)packet, source);
	    else if(packet instanceof ChatPacket)
		chat((ChatPacket)packet, source);
	    else
		Log.error("Server.handlePacket(): unknown packet: "+ packet);
	} catch(NetplayException e) {
	    Log.error("Server.handlePacket(): "+ e);
	}
		
    }


    /**
     */
    public void login(LoginPacket p, Client client) {
	int n = players.size();
	Player plr = new Player(client, p.getName());
	Ship s = new Ship(plr);
	plr.setShip(s);
	initPlayerShip(plr);
	players.put(client, plr); 
	try {
	    server.sendPacket(new LoginSuccessPacket(), client, true);
	    //server.sendPacket(new FocusPacket(s.getId()), client, true); 
	    server.sendPacket(new CavePacket(cave.getBitmap(), 
					     cave.getBitmapScale()), 
			      client, true);
	} catch(NetplayException e) {
	    Log.error("Server.login(): "+ e);
	}
	Log.log("client "+ client.getId() +": login");
    }


    /**
     */
    private void objectInfoRequest(ObjectInfoRequestPacket p, Client source) throws NetplayException {
	Log.debug("object info request for object "+ p.getObjectId());
	ServerObject o = (ServerObject)cave.getObject(p.getObjectId());
	if(o != null) {
	    String title = null;
	    if(o instanceof Ship) {
		Enumeration e = players.elements();
		while(e.hasMoreElements()) {
		    Player plr = (Player)e.nextElement();
		    if(plr.getShip() == o)
			title = plr.getName();
		}
	    }
	    Packet rp = new ObjectInfoReplyPacket(o.getId(), 
						  o.getModel().getName(),
						  title);
	    server.sendPacket(rp, source);
	} else {
	    Log.error("Server.objectInfoRequest: unknown model: "+ p.getObjectId());
	}	    
    }


    /**
     */
    private void modelRequest(ModelRequestPacket p, Client source) throws NetplayException {
	Log.debug("model request for model "+ p.getModelName());
	Model m = ModelFactory.getModelFactory().getModel(p.getModelName());
	if(m != null) {
	    ModelReplyPacket rp = new ModelReplyPacket(p.getModelName(), m);
	    server.sendPacket(rp, source, true);
	} else {
	    Log.error("Server.modelRequest(): unknown model: "+ p.getModelName());
	}
    }

    /**
     */
    private void console(ConsolePacket p, Client source) {
	Player plr = (Player)players.get(source);
	if(plr != null)
	    plr.getConsole().setStatus(p.getStatus());
	else
	    Log.error("Server.console(): unknown client: "+ source);
    }

    /**
     */
    private void chat(ChatPacket p, Client source) {
	Player plr = (Player)players.get(source);
	ChatPacket reply = new ChatPacket(plr.getName() +": "+ p.getText());
	for(Enumeration e=players.elements(); e.hasMoreElements(); ) {
	    plr = (Player)e.nextElement();
	    if(plr.getClient().isConnected())
		try {
		    server.sendPacket(reply, plr.getClient(), true);
		} catch(NetplayException ne) {
		    Log.error("Server.chat(): "+ ne);
		}
	}
    }

}
