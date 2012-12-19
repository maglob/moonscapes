package net.moonscapes.netplay.common;

/**
 */
public interface Command {
    static final byte
	LOGIN            = 1,  // C->S: engage normal packet oriented session
	ASK_PACKET_NAME  = 2,  // C->S: send int, return String, close
	ASK_PACKET_ID    = 3;  // C->S: send String, return int, close
}
