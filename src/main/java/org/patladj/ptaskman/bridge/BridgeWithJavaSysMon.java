package org.patladj.ptaskman.bridge;

import com.jezhumble.javasysmon.CpuTimes;
import org.patladj.ptaskman.model.ProcessList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.patladj.ptaskman.model.Process;
import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.ProcessInfo;
import org.patladj.ptaskman.util.Util;
import java.util.Comparator;

/**
 * Implements a bridge between the JavaSysMon library for process information and actions and the Taskmanager
 * @author PatlaDJ
 *
 */
public class BridgeWithJavaSysMon implements PsLibPTaskmanagerBridge {
	
	private JavaSysMon jsm=null;
	private CpuTimes cpuTimesSnapshot=null;
	private CpuTimes cpuTimesFirst=null;
	private Map<Long,Process> processMap = new HashMap<Long,Process>();


	public BridgeWithJavaSysMon() {
		jsm = new JavaSysMon();
		cpuTimesFirst=jsm.cpuTimes();
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

	private class ByPidComparator implements Comparator<ProcessInfo> {

		@Override
		public int compare(ProcessInfo o1, ProcessInfo o2) {
			return o2.getPid() - o1.getPid();
		}
	}

	private static final int limitProcessesCnt=9999;

	@Override
	public ProcessList getProcessList() {
		synchronized (this) {
			ProcessInfo[] pt = jsm.processTable();

			//Sort them by pid so they are given to the table the same order as the last refresh of the table
			Arrays.sort(pt, new ByPidComparator());

			ProcessList pl = new ProcessList();

			Map<Long,Process> newProcessMap = new HashMap<Long,Process>(); //We accumulate the new process list mapped by pid here. We still need the old one during the loop

			CpuTimes currCpuTimes=jsm.cpuTimes();
			long idleMillis=currCpuTimes.getIdleMillis();
			
			for (int i=0; (i<pt.length && i<limitProcessesCnt); i++) {
				if (pt[i].getTotalBytes() > 0) {
					// We build the process list here
					BridgeWithJavaSysMonProcess p = new BridgeWithJavaSysMonProcess();
					p.pid=pt[i].getPid();
					p.name=pt[i].getName();
					p.owner=pt[i].getOwner();

					// -------------- Begin calc CPU usage for a particular process --------------------------------------------------------------------
					//Get the current/previous CpuTimes for this process and potentially work with it
					CpuTimes currPrevCpuTimesSnapshot=null;
					try {
						currPrevCpuTimesSnapshot = ((BridgeWithJavaSysMonProcess)processMap.get(p.pid)).currPrevCpuTimesSnapshot;
					}
					catch (NullPointerException npe) {
						/* Ignore */
					}

					//calculate new cpuTimes specifically for this process
					if (currPrevCpuTimesSnapshot==null) {
						p.cpuUsagePercent=0.0f; //First invocation, the process will have 0 CPU usage until we gather enough data to bring overall CPU usage percent.
												//This happens between the first and the second invocation
						p.currPrevCpuTimesSnapshot = new CpuTimes(pt[i].getUserMillis(), pt[i].getSystemMillis(), idleMillis);
					}
					else {
						CpuTimes processCpuTimesIncomingSnapshot=new CpuTimes(pt[i].getUserMillis(), pt[i].getSystemMillis(), idleMillis);
						float cpuUsage = processCpuTimesIncomingSnapshot.getCpuUsage(currPrevCpuTimesSnapshot);
						p.cpuUsagePercent = cpuUsage;
						p.currPrevCpuTimesSnapshot=processCpuTimesIncomingSnapshot;
					}
					// <-------------------------------------------------------------------- End calc CPU usage for particular process -----------------

					p.memUsageMb =Util.roundToPrec(pt[i].getTotalBytes() / 1024.0f / 1024.0f, 2);

					pl.add(p);

					//We also fill a map with the processes indexed by pid for faster access later when info about particular process is requested
					newProcessMap.put(p.pid, p);
				}
			}

			processMap.clear();
			processMap=newProcessMap;
			return pl;
		}
	}

	@Override
	public void receiveProcessList(Object processList) {
		//This is potentially when the PsLib calls a refresh of the processes, not the other way around. Not yet implemented. And no need to be implemented (for now)
	}

	@Override
	public long getSystemUptimeSeconds() {
		return jsm.uptimeInSeconds();
	}

	@Override
	public int getSystemCPUCoresNum() {
//		return jsm.numCpus();
		return 999;
	}

	@Override
	public long getSystemCPUFrequencyHz() {
		return jsm.cpuFrequencyInHz();
	}

	@Override
	public String getSystemOSName() {
		return jsm.osName();
	}

	@Override
	public float getSystemCpuUsagePercent() {
		float ret=0.0f;

		if (this.cpuTimesSnapshot==null) {
			this.cpuTimesSnapshot=this.cpuTimesFirst;
			return ret; //Not enough history for accurate overall estimate of the processor usage
		}

		//Next request for cputumes should be atleast after 1 second since the previous for precise approximation of the CPU usage
		CpuTimes presentCpuTimes=jsm.cpuTimes();
		ret=presentCpuTimes.getCpuUsage(this.cpuTimesSnapshot);
		this.cpuTimesSnapshot=presentCpuTimes;

		return ret;
	}

	@Override
	public long getSystemRAMTotal() {
		return jsm.physical().getTotalBytes();
	}

	@Override
	public long getSystemRAMFree() {
		return jsm.physical().getFreeBytes();
	}

	@Override
	public long getSystemRAMInUse() {
		return jsm.physical().getTotalBytes() - jsm.physical().getFreeBytes();
	}

}
