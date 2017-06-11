package org.patladj.ptaskman.bridge;

import org.patladj.ptaskman.model.ProcessList;

import java.util.HashMap;
import java.util.Map;

import org.patladj.ptaskman.model.Process;
import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.ProcessInfo;

/**
 * Implements a bridge between the JavaSysMon library for process information and actions and the Taskmanager
 * @author PatlaDJ
 *
 */
public class BridgeWithJavaSysMon implements PsLibPTaskmanagerBridge {
	
	private JavaSysMon jsm=null;
	private Map<Long,Process> processMap = new HashMap<Long,Process>(); 

	public BridgeWithJavaSysMon() {
		jsm = new JavaSysMon();
	}
	
	@Override
	public Process getProcessInfo(long pid) {
		Process p = null;
		synchronized (this) {
			p = processMap.get(pid);
		}
		
		return p;
	}

	@Override
	public int getProcessKillstatus(long pid) {
		jsm.killProcess((int)pid);
		
		return 1;
	}

	@Override
	public ProcessList getProcessList() {
		ProcessInfo[] pt = jsm.processTable();
		
		ProcessList pl = new ProcessList();
		
		synchronized (this) {
			processMap.clear();
			
			for (int i=0; i<pt.length; i++) {
				// We build the process list here
				Process p = new Process();
				p.pid=pt[i].getPid();
				p.owner=pt[i].getOwner();
				p.name=pt[i].getName();
				p.cpuUsage=pt[i].getSystemMillis();
				p.memUsage=pt[i].getTotalBytes();
				
				pl.add(p);
				
				//We also fill a map with the processes indexed by pid for faster access later when info about particular process is requested
				processMap.put(p.pid, p);
			}
		}
		return pl;
	}

	@Override
	public void receiveProcessList(Object processList) {
		//This is potentially when the Lib calls a refresh of the processes, not the other way around. Not yet implemented.
	}

}
