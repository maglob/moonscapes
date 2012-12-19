package net.moonscapes.client;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import net.moonscapes.netplay.*;
import net.moonscapes.packet.*;
import net.moonscapes.phys.*;

/**
 * MoonScapes client.
 *
 * @author Marko Aalto
 * @version $Id: Client.java,v 1.30 2002/10/14 16:33:08 mka Exp $
 */
public final class Client extends Panel implements ClientHandler, Runnable {
    private static boolean optDebug;
    private static final String HELP_TEXT =
	"usage: java net.moonscapes.client.Client [<options>]\n"+
	"options:\n"+
	"\t-help Print this help message\n"+
	"\t-host <name>   Hostname of the server    default: localhost\n"+
	"\t-port <number> Port number on server     default: 9009\n"+
	"\t-name <name>   Set your name             default: Anon\n"+
	"controls:\n"+
	"\tTurn left     a\n"+
	"\tTurn right    s\n"+
	"\tThrust        right shift\n"+
	"\tFire          enter\n";
	
    private NetplayClient client;
    private ViewCanvas view;
    private ObjectCollection objects;
    private Vector pendingObjects;
    private Hashtable grobs, modelNameRequests;
    private ClientConsole console;
    private volatile boolean isRunning;
    private long packetWait = 1000;

    private int fps = 20;

    /**
     */
    public static void main(String[] args) {
	
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	//System.out.println("ge: "+ ge);
	GraphicsDevice[] gds = ge.getScreenDevices();
	for(int i=0; i<gds.length; i++)
	    System.out.println(""+ i +": "+ gds[i]);
	GraphicsDevice gd = ge.getDefaultScreenDevice();
	//System.out.println("gd: "+ gd);
	if(gd.isFullScreenSupported()) {
	    System.out.println("Fullscreen support ok.");
	} else {
	    System.out.println("No fullscreen support.");
	}
	

	if(hasOpt(args, "-help")) {
	    System.out.print(HELP_TEXT);
	    System.exit(0);
	}

	optDebug = hasOpt(args, "-debug");	
	String host = getOpt(args, "-host", "localhost");
	String name = getOpt(args, "-name", "Anon");
	int port = Integer.parseInt(getOpt(args, "-port", "9009"));

	Client client = null;
	try {
	    client = new Client(host, port, name);	
	} catch(NetplayException e) {
	    System.err.println("Client.main(): "+ e);
	    System.exit(0);
	}

	if(optDebug)
	    System.exit(0);
	
	Frame frame = new Frame("Moonscapes: "+ name +"@"+ host +":"+ port);
	frame.setSize(600, 480);
	frame.add(client);	
	class WinList extends WindowAdapter {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	}
	frame.addWindowListener(new WinList());
	frame.show();
    }


    /**
     */
    private static boolean hasOpt(String[] args, String name) {
	for(int i=0; i<args.length; i++)
	    if(args[i].equals(name))
		return true;
	return false;
    }


    /**
     */
    private static String getOpt(String[] args, String name, String def) {
	for(int i=0; i<args.length-1; i++)
	    if(args[i].equals(name))
		return args[i+1];
	return def;
    }


    /**
     */
    private Client(String host, int port, String name) throws NetplayException {
	client = Netplay.createClient(host, port, this);
	objects = new ObjectCollection();
	pendingObjects = new Vector();
	grobs = new Hashtable();
	setLayout(new BorderLayout());
	view = new ViewCanvas(objects);
	view.setLocation(0, 0);
	view.setSize(600, 480);
	view.setVisible(true);
	add(view, BorderLayout.CENTER);
	console = new ClientConsole();
	modelNameRequests = new Hashtable();
	view.addKeyListener(console);
	class FocusList extends FocusAdapter {
	    public void focusLost(FocusEvent e) {
		console.clear();
	    }
	}
	view.addFocusListener(new FocusList());

	client.connect();
	client.sendPacket(new LoginPacket(name), true);
	Thread t = new Thread(this, "main");
	isRunning = true;
	t.start();
    }


    
    /**
     */
    public void run() {
	int fps_time = 1000 / fps;
	ConsolePacket cp = new ConsolePacket();
	boolean skipFrame = false;
	while(isRunning) {
	    try {
		long t = System.currentTimeMillis();
		if(!skipFrame) {
		    view.setChatBuffer(console.getChatBuffer());
		    view.setConsoleStatus(console.getStatus());
		    //view.repaint();
		    view.render();
		}
		if(console.getChatText() != null) {
		    ChatPacket p = new ChatPacket(console.getChatText());
		    if(client.isConnected())
			client.sendPacket(p, true);
		    console.clearChat();
		}		
		long delay = fps_time - (System.currentTimeMillis() - t);
		view.setSleepTime(delay);
		if(delay > 0) {
		    Thread.sleep(delay);
		    skipFrame = false;
		} else {
		    Log.debug("missed frame redraw by "+ delay +" ms");
		    skipFrame = true;
		}
		cp.setStatus(console.getStatus());
		client.sendPacket(cp);
	    } catch(InterruptedException e) {
		Log.error("Client.run(): "+ e);
	    } catch(NetplayException npe) {
		Log.error("Client.run(): "+ npe);
	    }
	}
	Log.log("render loop ended");
	System.exit(0);
    }


    /**
     */
    private static void dumpThreads(String msg) {
	ThreadGroup tg = Thread.currentThread().getThreadGroup();
	Thread[] th = new Thread[tg.activeCount()];
	tg.enumerate(th);
	System.out.println(msg +": thread active count: "+ th.length);
	for(int i=0; i<th.length; i++) {
	    Thread t = th[i];
	    System.out.println("\t"+ t.getName() +", "+
			       (t.isAlive() ? "alive" : "dead") +", "+
			       (t.isDaemon() ? "daemon" : "user"));
	}
    }


    /**
     */
    private boolean isModelPending(String modelName) {
	for(int i=0; i<pendingObjects.size(); i++) {
	    ClientObject o = (ClientObject)pendingObjects.elementAt(i);
	    if(o.getGrob() instanceof PendingGrob) {
		PendingGrob pg = (PendingGrob)o.getGrob();
		if(pg.getModelName().equals(modelName)) 
		    return true;
	    }
	}
	return false;
    }


    /**
     */
    public void serverDisconnect() {
	isRunning = false;
	Log.debug("Server disconnect");
    }


    /**
     */
    public void handlePacket(Packet packet) {
	try {
	    if(packet instanceof UpdateObjectPacket) 
		updateObject((UpdateObjectPacket)packet);	
	    else if(packet instanceof ObjectInfoReplyPacket)
		objectInfoReply((ObjectInfoReplyPacket)packet);
	    else if(packet instanceof ModelReplyPacket) 
		modelReply((ModelReplyPacket)packet);
	    else if(packet instanceof FocusPacket) 
		focus((FocusPacket)packet);
	    else if(packet instanceof CavePacket)
		cave((CavePacket)packet);
	    else if(packet instanceof LoginSuccessPacket)
		;
	    else if(packet instanceof UpdateShotsPacket)
		updateShots((UpdateShotsPacket)packet);
	    else if(packet instanceof ChatPacket)
		chat((ChatPacket)packet);
	    else if(packet instanceof UpdateObjectsPacket) 
		updateObjects((UpdateObjectsPacket)packet);
	    else if(packet instanceof ScoreSheetPacket)
		scoreSheet((ScoreSheetPacket)packet);
	    else if(packet instanceof ShipAttributesPacket)
		shipAttributes((ShipAttributesPacket)packet);
	    else if(packet instanceof FramePacket)
		frame((FramePacket)packet);
	    else if(packet instanceof ExplosionPacket)
		explosion((ExplosionPacket)packet);
	    else
		Log.error("Client.handlePacket(): unknown packet: "+ packet);
	} catch(NetplayException e) {
	    Log.error("Client.handlePacket(): "+ e);
	}
    }


    /**
     */
    private void updateObject(UpdateObjectPacket p) throws NetplayException {
 	ClientObject o = (ClientObject)objects.getObject(p.getId());
	if(o == null) {
	    o = new ClientObject(p.getId());
	    o.setPos(p.getPos());
	    objects.addObject(o);
	}
	if(o.getGrob() == null) {
	    Long l = (Long)modelNameRequests.get(new Integer(o.getId()));
	    if(l==null || l.longValue()+packetWait<System.currentTimeMillis()) {
		ObjectInfoRequestPacket mp = new ObjectInfoRequestPacket(o.getId());
		client.sendPacket(mp);
		l = new Long(System.currentTimeMillis());
		modelNameRequests.put(new Integer(o.getId()), l);
	    }
	}
	o.setPos(p.getPos());
	o.setDir(p.getDir());
	//Log.debug("dir: "+ p.getDir());
    }


    /**
     */
    private void objectInfoReply(ObjectInfoReplyPacket p) throws NetplayException {
	Log.debug("objectInfoReply: "+ p.getObjectId() +", "+ p.getModelName()+
		  ", "+ p.getTitle());
	ClientObject o = (ClientObject)objects.getObject(p.getObjectId());
	if(o != null) {
	    o.setTitle(p.getTitle());
	    Grob g = (Grob)grobs.get(p.getModelName());
	    if(g != null)
		o.setGrob(g);
	    else {
		boolean askModel = !isModelPending(p.getModelName());
		o.setGrob(new PendingGrob(p.getModelName()));
		pendingObjects.addElement(o); 
		if(askModel) {
		    ModelRequestPacket mr = new ModelRequestPacket(p.getModelName());
		    client.sendPacket(mr, true);
		}
	    }		
	} else
	    Log.error("Client.modelNameReply(): unknown object: "+ p.getObjectId());
    }


    /**
     */
    private void modelReply(ModelReplyPacket p) {
	Log.debug("modelReply: "+ p.getModelName());
	Grob grob = new Grob(p.getModel());
	grobs.put(p.getModelName(), grob);
	synchronized (pendingObjects) {
	    for(int i=0; i<pendingObjects.size(); i++) {
		ClientObject o = (ClientObject)pendingObjects.elementAt(i);
		if(o.getGrob() instanceof PendingGrob) {
		    PendingGrob pg = (PendingGrob)o.getGrob();
		    if(pg.getModelName().equals(p.getModelName())) {
			o.setGrob(grob);
			pendingObjects.removeElementAt(i);
			i--;			
		    }
		}
	    }
	}
    }


    /**
     */
    private void focus(FocusPacket p) {
	/*
	MSObject o = objects.getObject(p.getObjectId());
	if(o == null) {
	    o = new ClientObject(p.getObjectId());
	    objects.addObject(o);
	} 
	*/
	view.setFocus(p.getPos());
	//view.repaint();
    }

    /**
     */
    private void cave(CavePacket p) {
	Bitmap bm = new Bitmap(p.getWidth(), p.getHeight());
	bm.setBytes(p.getBitmapBytes());
	view.setBitmap(bm, p.getScale());
    }

    /**
     */
    private void updateShots(UpdateShotsPacket p) {
	view.setShots(p.getShots());
    }

    /**
     */
    private void chat(ChatPacket p) {
	view.appendChatMessage(p.getText());
    }

    /**
     */
    private void updateObjects(UpdateObjectsPacket p) throws NetplayException {
	Enumeration e = objects.getObjects();
	while(e.hasMoreElements()) 
	    ((ClientObject)e.nextElement()).setVisible(false);

	e = p.getObjects().elements();
	while(e.hasMoreElements()) {
	    MSObject o = (MSObject)e.nextElement();
	    if(updateObjectHelper(o)) {
		ObjectInfoRequestPacket mp = new ObjectInfoRequestPacket(o.getId());
		client.sendPacket(mp);
		Long l = new Long(System.currentTimeMillis());
		modelNameRequests.put(new Integer(o.getId()), l);
	    }
	}
    }

    /**
     */
    private boolean updateObjectHelper(MSObject p) {
 	ClientObject o = (ClientObject)objects.getObject(p.getId());
	if(o == null) {
	    o = new ClientObject(p.getId());
	    o.setPos(p.getPos());
	    objects.addObject(o);
	}
	boolean askInfo = false;
	if(o.getGrob() == null) {
	    Long l = (Long)modelNameRequests.get(new Integer(o.getId()));
	    if(l==null || l.longValue()+packetWait<System.currentTimeMillis())
		askInfo = true;
	}
	o.setPos(p.getPos());
	o.setDir(p.getDir());
	o.setVisible(true);
	return askInfo;
    }

    /**
     */
    private void scoreSheet(ScoreSheetPacket p) {
	view.setScoreSheet(p.getScoreEntries());
    }

    /**
     */
    private void shipAttributes(ShipAttributesPacket p) {
	ClientObject obj = (ClientObject)objects.getObject(p.getId());
	if(obj != null) {
	    obj.setShieldOn(p.isShieldOn());
	    obj.setEnergy(p.getEnergy());	
	    obj.setMaxEnergy(p.getMaxEnergy());
	    obj.setThrustOn(p.isThrustOn());
	    obj.setVelocity(p.getVelocity());
	}
    }

    /**
     */
    private void frame(FramePacket p) {
	//long t = System.currentTimeMillis();
	//view.repaint();
	//view.render();
	//t = System.currentTimeMillis() -t;
	//System.out.println("time: "+ t);
    }

    /**
     */
    private void explosion(ExplosionPacket p) {
	ParticleFactory pf = view.getParticleFactory();
	pf.createSparks(p.getPos(), new Vec2f(), 0, 0, Math.PI*2,
			p.getForce(), p.getSparkCount()*2, 10);
	pf.createDebris(p.getPos(), new Vec2f(), 0, 0, Math.PI*2,
			  p.getForce()/2, p.getDebrisCount(), 40);
	ClientObject o = (ClientObject)objects.getObject(p.getObjectId());
	if(o != null) 
	    pf.createWrecks(p.getPos(), new Vec2f(), p.getForce()/2, 
			    o.getGrob(), 50);
	else
	    Log.warning("Client.explosion(): unknown object id: "+ p.getObjectId());
		
    }
}
