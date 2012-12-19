package net.moonscapes.packet;

import java.io.*;
import java.util.*;
import net.moonscapes.netplay.Packet;
import net.moonscapes.phys.*;

/**
 * Score sheet containing scores for all the players
 */
public class ScoreSheetPacket implements Packet {
    private Vector scores;

    /**
     */
    public ScoreSheetPacket() {
	this(null);
    }

    /**
     */
    public ScoreSheetPacket(Vector scores) {
	this.scores = scores;
    }

    /**
     */
    public void write(DataOutputStream out) throws IOException {
	out.writeInt(scores.size());
	for(int i=0; i<scores.size(); i++) {
	    ScoreEntry se = (ScoreEntry)scores.elementAt(i);
	    out.writeUTF(se.getName());
	    out.writeInt(se.getScore());
	}	    
    }

    /**
     */
    public void read(DataInputStream in) throws IOException {
	int n = in.readInt();
	scores = new Vector(n);
	for(int i=0; i<n; i++) {
	    ScoreEntry se = new ScoreEntry(in.readUTF(), in.readInt());
	    scores.addElement(se);
	}
    }


    /**
     */
    public Vector getScoreEntries() {
	return scores;
    }
}
