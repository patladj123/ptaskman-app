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
	 * Process cpu usage level (in percents)
	 */
	public int cpuUsagePercent;
	
	/**
	 * Process memory usage level (in Mb)
	 */
	public float memUsageMb;
	
	/**
	 * Constructor
	 */
	public Process() {}

	public String toJsonArray() {
		return "["+
					"\""+Util.JSString(this.pid+"")+"\","+
					"\""+Util.JSString(this.name)+"\","+
					"\""+Util.JSString(this.owner)+"\","+
					"\""+Util.JSString(this.cpuUsagePercent +"")+"\","+
					"\""+Util.JSString(this.memUsageMb +"")+"\""
				+"]";
	}
	
}
