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
 * @author Kyncho
 *
 */
@ServerEndpoint("/ptoperations")
public class WebsocketControl extends WebSocketServlet {
    //notice:not thread-safe
    private static ArrayList<Session> sessionList = new ArrayList<Session>();
    
    private static FrontEndControl fec=null;
    
    private static Pattern SEP_INCOMING_MESSAGE=Pattern.compile("^([^\\s+])\\s+(.*)", Pattern.DOTALL);
    
//    static {
//    	fec=FrontEndControl.getRunningInstance();
//    }
    
    public static ArrayList<Session> getSessionList() {
    	return sessionList;
    }
    
    @OnOpen
    public void onOpen(Session session) {
    	synchronized (getClass()) {
//          try {
            sessionList.add(session);
            //asynchronous communication
//            session.getBasicRemote().sendText("Hello!");
//        }catch(IOException e){}
    	}
    }
    
    @OnClose
    public void onClose(Session session){
        synchronized (getClass()) {
        	sessionList.remove(session);
        }
    }
    
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

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}