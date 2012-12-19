package net.moonscapes.server;

import net.moonscapes.phys.*;

/**
 */
class Shot extends ServerObject {
    private long createTime;
    private ServerObject parent;

    Shot() {
	this(null);
    }

    /**
     */
    Shot(ServerObject parent) {
	this.parent = parent;
	createTime = System.currentTimeMillis();
    }

    /**
     */
    long getCreateTime() {
	return createTime;
    }

    /**
     */
    ServerObject getParent() {
	return parent;
    }

    /**
     */
    String getModelName() {
	return "shot";
    }
}
