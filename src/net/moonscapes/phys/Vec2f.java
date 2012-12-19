package net.moonscapes.phys;

/**
 */
public class Vec2f {
    public float x, y;

    public Vec2f(float x, float y) {
	this.x = x;
	this.y = y;
    }

    public Vec2f() {
	this(0.0, 0.0);
    }

    public Vec2f(Vec2f v) {
	this(v.x, v.y);
    }

    public Vec2f(int x, int y) {
	this((float)x, (float)y);
    }

    public Vec2f(double x, double y) {
	this((float)x, (float)y);
    }

    public boolean equals(Object o) {
	if(o instanceof Vec2f) {
	    Vec2f v = (Vec2f)o;
	    return v.x==x && v.y==y;
	} else
	    return false;
    }

    public void set(Vec2f v) {
	set(v.x, v.y);
    }

    public void set(float x, float y) {
	this.x = x;
	this.y = y;
    }

    public void set(int x, int y) {
	set((float)x, (float)y);
    }

    public float length() {
	return (float)Math.sqrt(x*x + y*y);
    }

    public float lengthSquared() {
	return x*x + y*y;
    }

    public String toString() {
	return "("+ x +", "+ y +")";
    }

    public Vec2f add(Vec2f v) {
	return new Vec2f(x + v.x, y + v.y);
    }

    public void add(Vec2f v, Vec2f res) {
	res.x = x + v.x;
	res.y = y + v.y;
    }

    public Vec2f sub(Vec2f v) {
	return new Vec2f(x - v.x, y - v.y);
    }

    public void sub(Vec2f v, Vec2f res) {
	res.x = x - v.x;
	res.y = y - v.y;
    }

    public float dot(Vec2f v) {
	return x*v.x + y*v.y;
    }

    public Vec2f mul(float d) {
	return new Vec2f(x*d, y*d);
    }

    public void mul(float d, Vec2f res) {
	res.x = x * d;
	res.y = y * d;
    }

    public Vec2f normal() {
	float d = length();
	return new Vec2f(x/d, y/d);
    }

    public void normal(Vec2f res) {
	float d = length();
	res.x = x / d;
	res.y = y / d;
    }

    public static void main(String[] args) {
	Vec2f a = new Vec2f(1, 2);
	Vec2f b = new Vec2f(3.0, 4.0);
	Vec2f c = new Vec2f();
	System.out.println("a: "+ a);
	System.out.println("b: "+ b);
	System.out.println("a length: "+ a.length());
	System.out.println("a lengthSquared: "+ a.lengthSquared());
	a.add(b, c);
	System.out.println("a + b: "+ a.add(b) +" = "+ c);
	a.sub(b, c);
	System.out.println("a - b: "+ a.sub(b) +" = "+ c);
	System.out.println("a . b: "+ a.dot(b));
	a.mul(5, c);
	System.out.println("a mul 5: "+ a.mul(5) +" = "+ c);
	a.normal(c);
	System.out.println("a normal: "+ a.normal() +" = "+ c);
	System.out.println("a normal length: "+ c.length());
    }

}

