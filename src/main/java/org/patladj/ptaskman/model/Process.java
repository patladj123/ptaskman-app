package org.patladj.ptaskman.model;

import org.patladj.ptaskman.util.Util;

/**
 * Represents a single process with all if its information
 * @author PatlaDJ
 *
 */
public class Process {

	/**
	 * Process pid
	 */
	public long pid;
	
	/**
	 * Process name
	 */
	public String name;
	
	/**
	 * Process owner
	 */
	public String owner;
	
	/**
	 * Process cpu usage level (in percents from 0.0f to 1.0f)
	 */
	public float cpuUsagePercent;
	
	/**
	 * Process memory usage level (in Mb)
	 */
	public float memUsageMb;
	
	/**
	 * Constructor
	 */
	public Process() {}

	/**
	 * Converts this object into json array - my way
	 * @return The generated json string
	 */
	public String toJsonArray() {
		return "["+
					"\""+Util.JSString(this.pid+"")+"\","+
					"\""+Util.JSString(this.name)+"\","+
					"\""+Util.JSString(this.owner)+"\","+
					"\""+Util.JSString(Util.roundToPrec(this.cpuUsagePercent*100.0f,2) +"")+"\","+
					"\""+Util.JSString(this.memUsageMb +"")+"\""
				+"]";
	}
	
}
