package org.patladj.ptaskman.controller;

import org.patladj.ptaskman.bridge.BridgeWithJavaSysMon;
import org.patladj.ptaskman.bridge.PsLibPTaskmanagerBridge;
import org.patladj.ptaskman.model.Process;
import org.patladj.ptaskman.model.ProcessList;

import com.google.gson.Gson;
import org.patladj.ptaskman.util.Util;

import java.util.concurrent.TimeUnit;

/**
 * This class actions are making the logical connection between the front-end and the process management library interface by using the WebsocketControl's actions
 * @author PatlaDJ
 *
 */
public class FrontEndControl implements Runnable {
	/**
	 * To be able to access the running Thread publicly
	 */
	public volatile static Thread fecThread=null;
	
	/**
	 * Ref to the singleton instance of this class
	 */
	private volatile static FrontEndControl fec=null;
	
	/**
	 * Ref to the WebsocketControl's instance
	 */
	private volatile static WebsocketControl wsc=null;
	
	/**
	 * Used internally for locking threads around it
	 */
	private static Object lock=new Object();
	
	/**
	 * is still running flag
	 */
	private volatile boolean isRunning=true;

	/**
	 * Interface to the process management library
	 */
	private PsLibPTaskmanagerBridge psUtilLib = null;
	
	/**
	 * Used for json creation and parsing
	 */
	private Gson gson = null;
	
	
	/**
	 * Obtains a running singleton instance of this Runnable class
	 * @param theWsc Ref that is intended to be received from the WebsocketControl's instance
	 * @return
	 */
	public static FrontEndControl getRunningInstance(WebsocketControl theWsc) {
		wsc=theWsc; //Have a ref here
		
		synchronized (lock) {
			if (fec==null) {
				fec=new FrontEndControl();
				fecThread=new Thread(fec, "FrontEndControl Thread");
				fecThread.start();
			}
		}
		
		return fec;
	}
	
	
	/**
	 * 
	 * Parallel executing logic goes here
	 */
	@Override
	public void run() {
		psUtilLib = new BridgeWithJavaSysMon();
		gson=new Gson();
		
		while (this.isRunning) {
			System.out.println(" > One iteration...");
			String jsonData="";
			
			/**
			 * Deal with the data from the library that needs to be sent periodically to the front-end
			 */

			//#############################################################################################################################
			//Obtain a fresh process list object using the interface to the library
			ProcessList pl=psUtilLib.getProcessList();
			
			//Genrate a Json String for the process list
			jsonData="{ \"cmd\": \"procList\", \"cmdData\":"+pl.toJsonArray()+" }";
			
			//Send the data to all clients
			wsc.sendDataToAllTheClients(jsonData);
			//#############################################################################################################################
			//Genrate a Json String with a fresh data for the global resources
			double totRamInGb=Util.roundToPrec(psUtilLib.getSystemRAMTotal()/1024.0d/1024.0d/1024.0d,3);
			double usedRamInGb=Util.roundToPrec(psUtilLib.getSystemRAMInUse()/1024.0d/1024.0d/1024.0d,3);

			jsonData="{ \"cmd\": \"globRes\", \"cmdData\": {" +
					"\"osname\":\""+Util.JSString(psUtilLib.getSystemOSName())+"\"," +
					"\"cpucores\":\""+psUtilLib.getSystemCPUCoresNum()+"\"," +
					"\"cpufreq\":\""+Util.digits1Format.format(Util.roundToPrec(psUtilLib.getSystemCPUFrequencyHz()/1024.0d/1024.0d,1))+"\"," +
					"\"uptime\":\""+Util.secondsToReadable(psUtilLib.getSystemUptimeSeconds())+"\"," +
					"\"cpuinfo\":\""+Util.digits1Format.format(Util.roundToPrec(psUtilLib.getSystemCpuUsagePercent()*100,1))+"\"," +
					"\"raminfo\":\""+Util.digits3Format.format(usedRamInGb)+"Gb out of "+Util.digits3Format.format(totRamInGb)+"Gb\"," +
					"\"ramtot\":\""+Util.digits3Format.format(totRamInGb)+"\"," +
					"\"ramused\":\""+Util.digits3Format.format(usedRamInGb)+"\"" +
					"} }";

			//Send the data to all clients
			wsc.sendDataToAllTheClients(jsonData);
			//#############################################################################################################################
			
			try {
				synchronized (lock) {
					lock.wait(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(" --- Exiting the Thread safely");
	}
	
	/**
	 * Stops the thread gracefully
	 */
	public void stopIt() {
		this.isRunning=false;
		synchronized (lock) {
			lock.notifyAll();
		}
	}


	/**
	 * def destructor
	 */
	@Override
	protected void finalize() throws Throwable {
		this.stopIt();
		super.finalize();
	}


	/**
	 * Intended to be invoked from a WebsocketControl instance. Performs messageCommand actions defined by messageBody
	 * @param messageCommand What to do
	 * @param messageBody Parameters to execute the messageCommand with
	 */
	public void processClientCommand(String messageCommand, String messageBody) {
		switch (messageCommand) {
			case "kill_process":
				messageBody=messageBody.trim();
				long pid=Long.MIN_VALUE;
				try {
					pid=Long.parseLong(messageBody);
				}
				catch (NumberFormatException nfe) {
					System.err.println("ERROR: not a number pid received with the command for kill_process");
				}
				
				if (pid > 0) {
					psUtilLib.getProcessKillstatus(pid);
				}
			break;
			
			default: System.err.println("ERROR: Client sent unknown command '"+messageCommand+"'"); break;
		}
	}	
}
