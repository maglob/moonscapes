package net.moonscapes.phys;

import java.util.zip.*;

/**
 * Utility class to represent and exit 1-bit bitmaps.
 *
 * @author Marko Aalto
 * @version $Id: Bitmap.java,v 1.4 2001/11/02 00:46:45 mka Exp $
 */
public class Bitmap {
    private int width;
    private int height;
    private int mod;
    private int[] data;

    /**
     * Unit testing
     */
    public static void main(String[] args) throws Exception {
	Bitmap bm = new Bitmap(33, 20);
	System.out.println("1:\n"+ bm.printString());
	bm.set(0, 0);
	System.out.println("2:\n"+ bm.printString());
	bm.set(3, 0);
	System.out.println("3:\n"+ bm.printString());
	bm.set(4, 1);
	System.out.println("4:\n"+ bm.printString());
	bm.set(7, 3);
	System.out.println("5:\n"+ bm.printString());
	bm.set(31, 3);
	System.out.println("6:\n"+ bm.printString());
	bm.setRect(10, 0, 8, 4);
	System.out.println("7:\n"+ bm.printString());
	bm.clearRect(12, 1, 4, 2);
	System.out.println("7:\n"+ bm.printString());
	bm.setCircle(20, 5, 3, true);
	System.out.println("8:\n"+ bm.printString());
	byte[] buf = bm.getBytes();
	bm.clearRect(0, 0, 32, 20);
	bm.setCircle(15, 9, 7, true);
	System.out.println("9:\n"+ bm.printString());
	bm.setCircle(15, 9, 4, false);
	System.out.println("10:\n"+ bm.printString());
	bm.setBytes(buf);
	System.out.println("11:\n"+ bm.printString());
	
	System.out.println("buf len: "+ buf.length);
	Deflater def = new Deflater();
	def.setInput(buf);
	def.finish();
	byte[] comp = new byte[buf.length];
	int n = def.deflate(comp);
	System.out.println("def len: "+ n);

	Inflater inf = new Inflater();
	inf.setInput(comp);
	byte[] ib = new byte[buf.length];
	n = inf.inflate(ib);
	System.out.println("ib len: "+ ib.length + " ("+ n +")");
	bm.setBytes(ib);
	System.out.println("12:\n"+ bm.printString());
    }
    

    /**
     * Dont allow null constructor.
     */
    private Bitmap() {
    }

    /**
     */
    public Bitmap(int width, int height) {
	this.width = width;
	this.height = height;
	mod = (width + 31) / 32;
	data = new int[mod * height];
    }

    /**
     */
    public byte[] getBytes() {
	byte[] b = new byte[mod*height*4];
	int i=0;
	for(int y=0; y<height; y++)
	    for(int x=0; x<mod; x++) {
		int n = data[y*mod +x];
		b[i++] = (byte)((n >> 24) & 0xFF);
		b[i++] = (byte)((n >> 16) & 0xFF);
		b[i++] = (byte)((n >> 8) & 0xFF);
		b[i++] = (byte)(n & 0xFF);
	    }
	return b;
    }

    /**
     */
    public void setBytes(byte[] b) {
	int i=0;
	for(int y=0; y<height; y++)
	    for(int x=0; x<mod; x++) {
		int n = 0;
		n = b[i++] & 0xFF;
		n = (n<<8) | (b[i++] & 0xFF);
		n = (n<<8) | (b[i++] & 0xFF);
		n = (n<<8) | (b[i++] & 0xFF);
		data[y*mod +x] = n;
	    }
    }

    /**
     */
    public boolean isSet(int x, int y) {
	return x>=0 && x<width && y>=0 && y<height &&
	    (data[y*mod + x/32] & (1<<(31-(x%32)))) != 0;
    }

    /**
     */
    public void set(int x, int y) {
	if(x>=0 && x<width && y>=0 && y<height)
	    data[y*mod + x/32] |= (1<<(31-(x%32)));
    }

    /**
     */
    public void clear(int x, int y) {
	data[y*mod + x/32] &= ~(1<<(31-(x%32)));
    }

    /**
     */
    public void setRect(int x, int y, int w, int h) {
	for(int j=0; j<h; j++)
	    for(int i=0; i<w; i++)
		set(x+i, y+j);	
    }

    /**
     */
    public void clearRect(int x, int y, int w, int h) {
	for(int j=0; j<h; j++)
	    for(int i=0; i<w; i++)
		clear(x+i, y+j);	
    }

    /**
     */
    public void setHorLine(int xa, int xb, int y) {
	if(xa > xb) 
	    setHorLine(xb, xa, y);
	else 
	    for(int i=xa; i<=xb; i++)
		set(i, y);
    }

    /**
     */
    public void clearHorLine(int xa, int xb, int y) {
	if(xa > xb) 
	    clearHorLine(xb, xa, y);
	else
	    for(int i=xa; i<=xb; i++)
		clear(i, y);
    }

    /**
     */
    public void setCircle(int x, int y, int r, boolean fill) {
	int xx = 0;
	int yy = r;
	int d = 1 - r;
	setCirclePoints(x, y, xx, yy, fill);
	while(yy > xx) {
	    if(d < 0)
		d += 2*xx + 3;
	    else {
		d += 2*(xx-yy) + 5;
		yy--;
	    }
	    xx++;
	    setCirclePoints(x, y, xx, yy, fill);
	}
    }

    /**
     */
    private void setCirclePoints(int ox, int oy, int x, int y, boolean fill) {
	//set(ox+y, oy-x);
	//set(ox+x, oy-y);
	if(fill) {
	    setHorLine(ox+y, ox-y, oy-x);
	    setHorLine(ox+x, ox-x, oy-y);	
	    setHorLine(ox+y, ox-y, oy+x);
	    setHorLine(ox+x, ox-x, oy+y);
	} else {
	    clearHorLine(ox+y, ox-y, oy-x);
	    clearHorLine(ox+x, ox-x, oy-y);	
	    clearHorLine(ox+y, ox-y, oy+x);
	    clearHorLine(ox+x, ox-x, oy+y);
	}
    }


    private void setCirclePoints2(int ox, int oy, int x, int y) {
	set(ox+x, oy+y);
	set(ox+y, oy+x);
	set(ox+y, oy-x);
	set(ox+x, oy-y);
	set(ox-x, oy-y);
	set(ox-y, oy-x);
	set(ox-y, oy+x);
	set(ox-x, oy+y);
    }

    /**
     */
    public int getWidth() {
	return width;
    }

    /**
     */
    public int getHeight() {
	return height;
    }


    /**
     */
    public String printString() {
	StringBuffer sb = new StringBuffer();
	for(int y=0; y<height; y++) {
	    for(int x=0; x<width; x++)
		sb.append(isSet(x, y) ? '*' : ' ');
	    sb.append('\n');
	}
	return sb.toString();
    }

    /**
     */
    public String toString() {
	return "size: "+ width +", "+ height;
    }
}
