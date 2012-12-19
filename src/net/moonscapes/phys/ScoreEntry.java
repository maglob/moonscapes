package net.moonscapes.phys;

/**
 * Reprseting one row of information fro score sheet.
 *
 * @author Marko Aalto
 * @version $Id: ScoreEntry.java,v 1.1 2001/11/07 19:30:42 mka Exp $
 */
public class ScoreEntry {
    private String name;
    private int score;

    /**
     */
    public ScoreEntry(String name, int score) {
	this.name = name;
	this.score = score;
    }

    /**
     */
    public String getName() {
	return name;
    }

    /**
     */
    public int getScore() {
	return score;
    }
}
