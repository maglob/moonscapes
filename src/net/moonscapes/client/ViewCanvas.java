package net.moonscapes.client;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import net.moonscapes.phys.*;

/* opt ideas
 * RM_BASIC:
 *   - scroll map, draw rest of the map % objects
 *   - keep copy of old positions and remove just before painting again
 * RM_DB_OFF_SCREEN
 *   - Try BufferedImage
 * RM_DB_COPYAREA2
 *   - Not much to do, seems to work on screen visible pixels...
 */

/**
 * Canvas for viewing the actual game in action.
 *
 * @author Marko Aalto
 * @version $Id: ViewCanvas.java,v 1.21 2001/11/22 12:16:28 mka Exp $
 */
class ViewCanvas extends Canvas {
    private static boolean optUseVolatileImage = true;
    public static final int
	RM_BASIC         = 1,
	RM_DB_OFF_SCREEN = 2;
    private int renderMethod = RM_DB_OFF_SCREEN;
    private ObjectCollection objects;
    private Vector shots;
    private Vec2f focus;
    private Image dblBuffer;
    private VolatileImage volImage;
    private Graphics volGraphics, screenGraphics;
    private Grob defaultGrob;
    private Bitmap bitmap;
    private LineBitmap lineBitmap;
    private int bitmapScale;
    private String chatBuffer;
    private Vector chatMessages;
    private int chatMsgSize = 6;
    private int chatRemoveDelay = 10000;
    private long chatTimestamp;
    private Vector scoreSheet;
    private int consoleStatus;
    private Color 
	grobColor       = Color.white,
	caveColor       = Color.gray,
	titleColor      = Color.lightGray,
	chatBufferColor = Color.yellow,
	chatMsgColor    = Color.yellow,
	scoreColor      = Color.red,
	hudColor        = Color.red.brighter(),
	debrisColor     = Color.white;
    private long timeClear, timePaint, timeBlit, timeLast, timeSleep;
    private boolean optScoreSheet = true;
    private int frames;
    private ParticleFactory particleFactory;
    private Font 
	defaultFont = new Font("Default", Font.PLAIN, 12),
	scoreFont = new Font("Default", Font.PLAIN, 12),
	scoreTopFont = new Font("Default", Font.BOLD, 12);


    /**
     */
    ViewCanvas(ObjectCollection objects) {
	this.objects = objects;
	this.focus = new Vec2f();
	shots = new Vector();
	setBackground(Color.black);
	setForeground(Color.white);
	defaultGrob = new Grob(new DefaultModel());
	chatMessages = new Vector();
	particleFactory = new ParticleFactory(512);
	
	class CompList extends ComponentAdapter {
	    public void componentResized(ComponentEvent e) {
		Dimension size = e.getComponent().getSize();
		if(dblBuffer != null) 
		    dblBuffer.flush();
		dblBuffer = createImage(size.width, size.height);
		if(optUseVolatileImage) {
		    volImage = createVolatileImage(size.width, size.height);
		    boolean b = volImage.validate(getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE;

		    ImageCapabilities ic = volImage.getCapabilities();
		    System.out.println("vimg: "+ volImage +", "+ b +", "+
				       ic.isAccelerated() +", "+ ic.isTrueVolatile());

		}
		volGraphics = null;

		if(screenGraphics != null) {
		    screenGraphics.dispose();
		    screenGraphics = null;
		}


	    }
	}
	addComponentListener(new CompList());
	class KeyList extends KeyAdapter {
	    public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_F1)
		    renderMethod = (renderMethod++) % 2 + 1;
	    }
	}
	addKeyListener(new KeyList());
    }


    /**
     */
    ParticleFactory getParticleFactory() {
	return particleFactory;
    }






    /**
     */
    void setConsoleStatus(int status) {
	consoleStatus = status;
    }

    /**
     */
    void setChatBuffer(String s) {
	chatBuffer = s;
    }


    /**
     */
    void appendChatMessage(String msg) {
	chatMessages.insertElementAt(msg, 0);
	if(chatMessages.size() > chatMsgSize)
	    chatMessages.removeElementAt(chatMsgSize);
	chatTimestamp = System.currentTimeMillis();
    }


    /**
     */
    void setSleepTime(long t) {
	timeSleep = t;
    }


    /**
     */
    void setScoreSheet(Vector v) {
	scoreSheet = v;
    }


    /**
     */
    void render() {
	long t = System.currentTimeMillis();
	if(screenGraphics == null) 
	    screenGraphics = getGraphics();
	update(screenGraphics);
	t = System.currentTimeMillis() - t;
	//System.out.println("render: "+ t);

	/*
	Graphics g = getGraphics();
	if(g != null) {
	    update(g);
	    g.dispose();
	}
	*/
    }


    /**
     */
    public void update(Graphics graph) {	
	boolean getTime = timeLast + 1000 < System.currentTimeMillis();
	if(getTime)
	    timeLast = System.currentTimeMillis();
	long t;

	switch(renderMethod) {
	case RM_DB_OFF_SCREEN:
	    if(dblBuffer == null)
		break;
	    Graphics g;
	    if(optUseVolatileImage) {
		if(volGraphics == null) {
		    if(volImage == null)
			return;
		    volGraphics = volImage.getGraphics();
		}
	    } else {
		if(volGraphics == null) {
		    volGraphics = dblBuffer.getGraphics();
		}
	    }
	    g = volGraphics;
		
	    t = System.currentTimeMillis();
	    g.clearRect(0, 0, getSize().width, getSize().height);
	    if(getTime)
		timeClear = System.currentTimeMillis() - t;	    
	    t = System.currentTimeMillis();
	    paint(g);
	    if(getTime)
		timePaint = System.currentTimeMillis() - t;
	    //g.dispose();
	    t = System.currentTimeMillis();
	    if(optUseVolatileImage)
		graph.drawImage(volImage, 0, 0, this);
	    else
		graph.drawImage(dblBuffer, 0, 0, null);

	    if(getTime)
		timeBlit = System.currentTimeMillis() - t;
	    break;

	case RM_BASIC:
	    t = System.currentTimeMillis();
	    graph.clearRect(0, 0, getSize().width, getSize().height);
	    if(getTime)
		timeClear = System.currentTimeMillis() - t;
	    t = System.currentTimeMillis();
	    paint(graph);
	    if(getTime)
		timePaint = System.currentTimeMillis() - t;
	    timeBlit = 0;
	    break;
	    
	default:
	    System.out.println("ViewCanvas.update(): unknown render method");
	}
    }


    /**
     */
    public void paint(Graphics g) {	
	frames++;

	if(focus == null) 
	    return;

	int width = getSize().width;
	int height = getSize().height;


	int tx = (width / 2) - (int)focus.x;
	int ty = (height / 2) + (int)focus.y;

	g.translate(tx, ty);
	g.setColor(caveColor);
	paintBitmap(g, bitmap);
	g.setColor(grobColor);
	for(Enumeration e=objects.getObjects(); e.hasMoreElements(); ) {
	    ClientObject obj = (ClientObject)e.nextElement();
	    if(!obj.isVisible())
		continue;

	    Grob grob = obj.getGrob();
	    if(grob == null)
		grob = defaultGrob;
	    Vec2f pos = obj.getPos();
	    g.translate((int)pos.x, -(int)pos.y);
	    g.setColor(grobColor);
	    grob.paint(g, obj.getDir());
	    int r = (int)grob.getRadius() + 1;
	    if(obj.getTitle() != null) {
		g.setColor(titleColor);
		g.drawString(obj.getTitle(), -r, r+10);
		g.setColor(grobColor);
	    }
	    if(obj.isShieldOn()) {
		int sa = ((frames / 1) % 36) * 5;
		int d = 40;
		for(int i=0; i<360; i+=d)
		    //g.drawOval(-r, -r, r*2, r*2, sa+i, sa+i+10);
		    g.drawArc(-r, -r, r*2, r*2, sa+i, 15);
	    }
	    if(obj.isThrustOn()) {
		particleFactory.createSparks(obj.getPos(), obj.getVelocity(), 
					     obj.getDir()-Math.PI, 
					     5, Math.PI/3, 5, 4, 8);
	    }
	    int en = obj.getEnergy() / 2;
	    int me = obj.getMaxEnergy() / 2;
	    g.setColor(hudColor);
	    if(obj.getPos().equals(focus)) {
		g.translate(50, 25);
		g.fillRect(0, -en, 4, en);
		g.drawRect(-1, -me-1, 5, me+1);
		g.translate(-50, -25);
	    } else if(false && me > 0) {
		g.translate(r+2, 12);
		g.fillRect(0, -en/2, 2, en/2);
		g.drawLine(0, -me/2, 1, -me/2);
		g.translate(-(r+2), -12);
	    }
	    g.translate(-(int)pos.x, (int)pos.y);
	}

	g.setColor(grobColor);
	for(Enumeration e=shots.elements(); e.hasMoreElements(); ) {
	    Vec2f v = (Vec2f)e.nextElement();
	    g.drawRect((int)v.x, -(int)v.y, 1, 1);
	}
	g.setColor(grobColor);

	particleFactory.paint(g);
	particleFactory.update();

	g.setColor(grobColor);
	g.translate(-tx, -ty);

	g.setFont(defaultFont);
	g.drawString("Objects: "+ objects.getSize() +" "+ shots.size() +" "+
		     particleFactory.getSize(), 0, 10);
	g.drawString("Focus: "+ (int)(focus.x+.5f) +", "+
		     (int)(focus.y+.5f), 120, 10);
	long t = timeClear + timePaint + timeBlit;
	g.drawString("Time: "+ t +": "+ timeClear +" "+ timePaint +" "+ 
		     timeBlit +" ("+ timeSleep +")", 255, 10);
	g.drawString("Render: "+ renderMethod, 420, 10);
	String sk = Integer.toBinaryString(consoleStatus);
	while(sk.length() < 5)
	    sk = "0" + sk;
	g.drawString("Keys: "+ sk, 500, 10);

	if(chatBuffer != null) {
	    g.setColor(chatBufferColor);
	    g.drawString("Chat: "+ chatBuffer, width/2-64, 
			 height/2+50);
	}


	if(chatTimestamp + chatRemoveDelay < System.currentTimeMillis()) {
	    if(chatMessages.size() > 0)
		chatMessages.removeElementAt(chatMessages.size()-1);
	chatTimestamp = System.currentTimeMillis();
	}

	int n = 11;
	for(int i=0; i<chatMessages.size(); i++) {
	    String s = (String)chatMessages.elementAt(i);
	    g.setColor(Color.black);
	    g.drawString(s, 4+1, height-i*n-4+1);
	    g.setColor(chatMsgColor);
	    g.drawString(s, 4, height-i*n-4);
	}

	if(optScoreSheet) {
	    g.translate(width-110, 32);
	    g.setColor(scoreColor);
	    paintScoreSheet(g, scoreSheet);
	    g.translate(-(width-110), -32);
	}


    }


    /**
     */
    private void paintScoreSheet(Graphics g, Vector v) {
	if(v != null) {
	    Enumeration e = v.elements();
	    int y = 0;
	    int dx = 11;
	    boolean isFirst = true;
	    while(e.hasMoreElements()) {
		ScoreEntry se = (ScoreEntry)e.nextElement();
		g.setFont(isFirst ? scoreTopFont : scoreFont);
		g.drawString(se.getName(), 0, y);
		g.drawString(""+se.getScore(), 80, y);
		if(isFirst)
		    g.drawLine(0, y, 100, y);
		y += dx;
		isFirst = false;
	    }
	}
    }

    /**
     */
    private void paintBitmap(Graphics g, Bitmap bm) {
	if(bm != null) {
	    if(lineBitmap == null) {
		lineBitmap = new LineBitmap(bm, bitmapScale);
	    }
	    lineBitmap.paint(g);	
	}
    }

    /**
     */
    void setShots(Vector s) {
	shots = s;
    }

    /**
     */
    void setBitmap(Bitmap bm, int scale) {
	bitmap = bm;
	bitmapScale = scale;
	particleFactory.setBitmap(bitmap, scale);
    }

    /**
     */
    void setFocus(Vec2f newFocus) {
	focus.set(newFocus);
    }

    /**
     */
    /* MSObject getFocus() {
	return focus;
	} */
}
