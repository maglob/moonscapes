package net.moonscapes.client;

import net.moonscapes.phys.*;

/**
 * Temporary Grob used for objects whose model is still unknown.
 *
 * @author Marko Aalto
 * @version $Id: PendingGrob.java,v 1.1 2001/10/21 17:54:02 mka Exp $
 * @see Grob
 */
class PendingGrob extends Grob {
    private String modelName;

    /**
     */
    PendingGrob(String modelName) {
	super(new DefaultModel());
	this.modelName = modelName;
    }

    /**
     */
    String getModelName() {
	return modelName;
    }

}
