package org.patladj.ptaskman.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.websocket.Session;

import org.patladj.ptaskman.bridge.BridgeWithJavaSysMon;
import org.patladj.ptaskman.bridge.PsLibPTaskmanagerBridge;
import org.patladj.ptaskman.model.ProcessList;
import org.patladj.ptaskman.model.Process;

import com.google.gson.Gson;

public class FrontEndControl implements Runnable {
	/**
	 * To be able to access the running Thread publicly
	 */
	public volatile static Thread fecThread=null;
	
	private volatile static FrontEndControl fec=null;
	private static Object lock=new Object();
	
	private volatile boolean isRunning=true;

	private PsLibPTaskmanagerBridge psUtilLib = null;

	private ArrayList<Session> sessionList = null;
	
	private Gson gson = null;
	
	public static FrontEndControl getRunningInstance() {
		synchronized (lock) {
			if (fec==null) {
				fec=new FrontEndControl();
				fecThread=new Thread(fec, "FrontEndControl Thread");
//				fecThread.setDaemon(true);
				fecThread.start();
			}
		}
		
		return fec;
	}
	
	
	
	@Override
	public void run() {
//		psUtilLib = new BridgeWithJavaSysMon();
		sessionList = WebsocketControl.getSessionList();
//		gson=new Gson();
		
		while (this.isRunning) {
			System.out.println(" > One iteration...");
			
			/**
			 * Deal with the data from the library that needs to be sent periodically to the front-end
			 */
			
			//Obtain a fresh process list object using the interface to the library
//			ProcessList pl=psUtilLib.getProcessList();
			Process p=new Process();
			p.cpuUsage=500;
			p.memUsage=400;
			p.name="RUNDLL.exe";
			p.owner="Administrator";
			p.pid=200;
			ProcessList pl=new ProcessList();
			pl.add(p);
			
			//Genrate a Json String
//			String jsonData=gson.toJson(pl);
			String jsonData="kur";
			
			//Send the data to all clients
			try {
				for (Session session : sessionList) {
					//Send the data to all the visitors of the webapp page
					session.getBasicRemote().sendText(jsonData);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				synchronized (lock) {
					lock.wait(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopIt() {
		this.isRunning=false;
		synchronized (lock) {
			lock.notifyAll();
		}
	}



	@Override
	protected void finalize() throws Throwable {
		this.stopIt();
		super.finalize();
	}



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
