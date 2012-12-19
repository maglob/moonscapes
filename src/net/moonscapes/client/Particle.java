package net.moonscapes.client;

import java.awt.*;
import net.moonscapes.phys.*;

/**
 * Particle used for client side effects
 *
 * @author Marko Aalto
 * @version $Id: Particle.java,v 1.1 2001/11/22 12:22:32 mka Exp $
 * @see Spark
 * @see Debris
 */
interface Particle {
    /**
     */
    void update(Bitmap bitmap, int bitmapScale);

    /**
     */
    void paint(Graphics g);

    /**
     */
    boolean isDead();
}
