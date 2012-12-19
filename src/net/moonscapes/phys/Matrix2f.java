package net.moonscapes.phys;

/**
 */
public class Matrix2f {
    public Vec2f a, b;

    public Matrix2f() {
	this(1, 0, 0, 1);
    }

    public Matrix2f(int ax, int ay, int bx, int by) {
	this((float)ax, (float)ay, (float)bx, (float)by);
    }

    public Matrix2f(float ax, float ay, float bx, float by) {
	this(new Vec2f(ax, ay), new Vec2f(bx, by));
    }

    public Matrix2f(double ax, double ay, double bx, double by) {
	this((float)ax, (float)ay, (float)bx, (float)by);
    }

    public Matrix2f(Vec2f a, Vec2f b) {
	this.a = a;
	this.b = b;
    }

    public Matrix2f(double angle) {
	double ca = Math.cos(angle);
	double sa = Math.sin(angle);
	a = new Vec2f(ca, sa);
	b = new Vec2f(-sa, ca);
    }


    public Vec2f mul(Vec2f v) {
	return new Vec2f(v.x*a.x + v.y*b.x, v.x*a.y + v.y*b.y);
    }

    public void mul(Vec2f v, Vec2f res) {
	float vx = v.x;   // Dont modify v if v is the same object as res
	float vy = v.y;
	res.x = vx*a.x + vy*b.x;
	res.y = vx*a.y + vy*b.y;
    }

    public Matrix2f mul(Matrix2f m) {
	return new Matrix2f(a.x*m.a.x + a.y*m.b.x,
			    a.x*m.a.y + a.y*m.b.y,
			    b.x*m.a.x + b.y*m.b.x,
			    b.x*m.a.y + b.y*m.b.y);
			    
    }

    public String toString() {
	return "["+ a +", "+ b +"]";
    }
    
    public static void main(String[] args) {
	Matrix2f m = new Matrix2f(1, 0, 0, 1);
	Matrix2f n = new Matrix2f(2, 3, 4, 5);
	double d = Math.PI/2;
	Matrix2f r = new Matrix2f(d);
	Vec2f a = new Vec2f(4, 5); 
	Vec2f c = new Vec2f();
	System.out.println("matrix m: "+ m);
	System.out.println("matrix n: "+ n);
	System.out.println("matrix r: "+ r + " angle "+ d);
	System.out.println("vector a: "+ a);
	m.mul(a, c);
	System.out.println("m mul a: "+ m.mul(a) +" = "+ c); 
	System.out.println("m mul n: "+ m.mul(n));
	System.out.println("n mul n: "+ n.mul(n));
	System.out.println("m mul r: "+ m.mul(r));

    }
    
}
