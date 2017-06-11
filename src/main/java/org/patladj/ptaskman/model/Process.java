package org.patladj.ptaskman.model;

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
	 * Process cpu usage level
	 */
	public long cpuUsage;
	
	/**
	 * Process memory usage level
	 */
	public long memUsage;
	
	/**
	 * Constructor
	 */
	public Process() {}
	
}
