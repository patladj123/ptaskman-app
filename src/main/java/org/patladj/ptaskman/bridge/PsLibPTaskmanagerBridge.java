package org.patladj.ptaskman.bridge;

import org.patladj.ptaskman.model.ProcessList;
import org.patladj.ptaskman.model.Process;

/**
 * The interface is being implemented by different types of libraries (PSLib) that are used for fetching process information and process manipulation.
 * The methods which start with get are requests to the TaskManager to the PSLib.
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
	
}
