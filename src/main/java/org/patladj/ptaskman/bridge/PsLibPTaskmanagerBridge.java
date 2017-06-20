package org.patladj.ptaskman.bridge;

import org.patladj.ptaskman.model.ProcessList;
import org.patladj.ptaskman.model.Process;

/**
 * The interface is being implemented by different types of libraries (PSLib) that are used for fetching process information and process manipulation.
 * The methods which start with get are requests from the TaskManager to the PSLib.
 * The methods which start with receive are data received from the PSLib to the TaskManager.
 * @author PatlaDJ
 *
 */
public interface PsLibPTaskmanagerBridge {
	
	/**
	 * Asks the cross-platform library about info about particular process 
	 * @param pid - The pid of the process
	 */
	public Process getProcessInfo(long pid);
	
	/**
	 * Kills the process designated by pid
	 * @param pid The pid of the process which is intended to be killed
	 * @return status The status resulting the kill command that is returned from the library. If the status is successful 1 will be returned
	 */
	public int getProcessKillstatus(long pid);
	
	/**
	 * This method is when the TaskManager is requesting a newly refreshed process list from the PSLib
	 * @return a ProcessList
	 */
	public ProcessList getProcessList();
	
	/**
	 * This method is only executed by the PSLib and is intended to trigger events on the TaskManager
	 * @param processList Receives a process list in undefined format which is meant to be converted into a ProcessList in the implementation
	 */
	public void receiveProcessList(Object processList);

	/**
	 * This method is when the TaskManager is requesting a newly refreshed uptime in seconds from the PSLib
	 * @return Uptime in measured in seconds
	 */
	public long getSystemUptimeSeconds();

	/**
	 * This method is when the TaskManager is requesting a newly refreshed number of CPU cores from the PSLib
	 * @return Number of CPU cores
	 */
	public int getSystemCPUCoresNum();

	/**
	 * This method is when the TaskManager is requesting a newly refreshed current CPU frequency from the PSLib
	 * @return Current CPU frequency in Hz
	 */
	public long getSystemCPUFrequencyHz();

	/**
	 * This method is when the TaskManager is requesting a newly refreshed name of the OS from the PSLib
	 * @return The OS Name
	 */
	public String getSystemOSName();

	/**
	 * This method is when the TaskManager is requesting a newly refreshed global CPU usage from the PSLib
	 * @return The average global CPU usage in percents (0 is 0%, 1.0 is 100%) since the last invocation of this method
	 */
	public float getSystemCpuUsagePercent();

	/**
	 * This method is when the TaskManager is requesting a newly refreshed total memory info from the PSLib
	 * @return Total memory (RAM) installed on the platform. In bytes
	 */
	public long getSystemRAMTotal();

	/**
	 * This method is when the TaskManager is requesting a newly refreshed free memory info from the PSLib
	 * @return Free memory (RAM) on the platform. In bytes
	 */
	public long getSystemRAMFree();

	/**
	 * This method is when the TaskManager is requesting a newly refreshed memory that is in use info from the PSLib
	 * @return Memory (RAM) that is in use on the platform. In bytes
	 */
	public long getSystemRAMInUse();
}
