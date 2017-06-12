package org.patladj.ptaskman.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;


/**
 * This is the websocket which is used for receiving and sending messages from and to the front-end part
 * @author PatlaDJ
 *
 */
@SuppressWarnings("deprecation")
@ServerEndpoint("/ptoperations")
public class WebsocketControl extends WebSocketServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 110283178240290856L;

	/**
	 * List with all sessions (clients) which are connected using from the webpage interface
	 */
    private static ArrayList<Session> sessionList = new ArrayList<Session>();
    
    /**
     * Ref to the FrontEndControl singleton instance
     */
    private static FrontEndControl fec=null;
    
    /**
     * Parsing incoming message string by using this pattern
     */
    private static Pattern SEP_INCOMING_MESSAGE=Pattern.compile("^([^\\s+])\\s+(.*)", Pattern.DOTALL);
    
//    public static ArrayList<Session> getSessionList() {
//    	return sessionList;
//    }
    
    /**
     * Things that are done when a new client connects on the webpage
     * @param session Received new session
     */
    @OnOpen
    public void onOpen(Session session) {
    	fec=FrontEndControl.getRunningInstance(this);
    	
    	synchronized (getClass()) {
//          try {
            sessionList.add(session);
            //asynchronous communication
//            session.getBasicRemote().sendText("Hello!");
//        }catch(IOException e){}
    	}
    }
    
    /**
     * Things that are done where a client is disconnected from the webpage
     * @param session Given session which this client had
     */
    @OnClose
    public void onClose(Session session){
        synchronized (getClass()) {
        	sessionList.remove(session);
        }
    }
    
    /**
     * Registers a new message received on the websocket for this particular client
     * @param msg The string message
     */
    @OnMessage
    public void onMessage(String msg) {
    	System.out.println(" ** Received message: |"+msg+"|");
    	
    	//Separate message in 2 parts, command and arguments
    	String messageBody=null;
    	String messageCommand=null;
    	
    	Matcher m=null;
    	if (null != (m=SEP_INCOMING_MESSAGE.matcher(msg))) {
			if (m.find()) {
				messageBody=m.group(2);
				messageCommand=m.group(1).trim();
			}
		}
    	
    	if (messageBody==null || messageCommand==null) {
    		System.err.println("ERROR: Defective message received from client. Message was '"+msg+"'");
    	}
    	
    	//Send the received message to the FrontEndController instance for processing
    	synchronized (getClass()) {
    		fec.processClientCommand(messageCommand, messageBody);
		}
    }
    
    /**
     * Sends data to all the clients which are connected on the webpage
     * @param jsonString
     */
    public void sendDataToAllTheClients(String jsonString) {
    	synchronized (getClass()) {
    		try {
    			for (Session session : sessionList) {
    				//Send the data to all the visitors of the webapp page
    				session.getBasicRemote().sendText(jsonString);
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}