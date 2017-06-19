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
		ProcessInfo[] pt = jsm.processTable();

		//Sort them by pid so they are given to the table the same order as the last refresh of the table
		Arrays.sort(pt, new ByPidComparator());

		ProcessList pl = new ProcessList();
		
		synchronized (this) {
			processMap.clear();
			
			for (int i=0; (i<pt.length && i<limitProcessesCnt); i++) {
				if (pt[i].getTotalBytes() > 0) {
					// We build the process list here
					Process p = new Process();
					p.pid=pt[i].getPid();
					p.name=pt[i].getName();
					p.owner=pt[i].getOwner();
					p.cpuUsagePercent = (int)Util.rand(0, 100); //Temporary fix until I make the lib properly obtain cross-platform CPU usage percent per process
					p.memUsageMb =Util.roundToPrec(pt[i].getTotalBytes() / 1024.0f / 1024.0f, 2);

					pl.add(p);

					//We also fill a map with the processes indexed by pid for faster access later when info about particular process is requested
					processMap.put(p.pid, p);
				}
			}
		}
		return pl;
	}

	@Override
	public void receiveProcessList(Object processList) {
		//This is potentially when the Lib calls a refresh of the processes, not the other way around. Not yet implemented.
	}

	@Override
	public long getSystemUptimeSeconds() {
		return jsm.uptimeInSeconds();
	}

	@Override
	public int getSystemCPUCoresNum() {
		return jsm.numCpus();
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

		//Next request for cputumes should be atleast after 1 second since the previous
		CpuTimes presentCpuTimes=jsm.cpuTimes();
		ret=presentCpuTimes.getCpuUsage(this.cpuTimesSnapshot);
		this.cpuTimesSnapshot=presentCpuTimes;

		//Attempt to get CPU percentage for a given pid
		/*ProcessInfo[] psinfo = js.processTable();
		publicstaticvoidprocessInfo(intprocessId) {new JavaSysMon();//get a list of

			Process infoProcessInfo pstemp;
			pstemp = psinfo[i];
			systemMillis = pstemp.getSystemMillis();
			userMillis = pstemp.getUserMillis();
		}
	}
	CpuTimes cput =

					System.out.println("processId:"+
					processId);
					System.out.println("processId
	systemMillis:"+ systemMillis);
					System.out.println("processId userMillis:"+
	userMillis);
	System.out.println("processId CPU
	Utilization:"+ util);
}
 longuserMillis = 0, systemMillis = 0;for(inti=0; i<psinfo.length;i++)
		{intpid =

		pstemp.getPid();if(pid == processId) {break;new CpuTimes(userMillis,
		systemMillis, 0);floatutil = cput.getCpuUsage(cput) *
		100; */

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
