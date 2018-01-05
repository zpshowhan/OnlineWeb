package com.websocket.online.server;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/** 
* @ClassName: WebsocketServer 
* @Description: websocket 服务端 
* @Company:esmart
* @author Administrator 
* @version 1.0 2016年8月19日 上午9:37:08 
*/
@ServerEndpoint("/webserver")
public class WebsocketServer {

	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    
	private Session session;
	private static Vector<Session> sessions=new Vector<Session>();
	private static CopyOnWriteArraySet<WebsocketServer> webSocketSet = new CopyOnWriteArraySet<WebsocketServer>();
	private static ConcurrentHashMap<Session,WebsocketServer> sionWebMap=new ConcurrentHashMap<Session,WebsocketServer>();
	@OnOpen
	public void onOpen(Session session){
		this.session=session;
		if(sessions.size()>0){
			for(Session son:sessions){
				if(!son.equals(session)){
					sessions.add(session);
					webSocketSet.add(this);
					sionWebMap.put(session, this);
					addOnlineCount();
				}
			}
		}else{
			sessions.add(session);
			webSocketSet.add(this);
			sionWebMap.put(session, this);
			addOnlineCount();
		}
		
		System.out.println("开启连接: "+session.hashCode());
	}
	
	@OnMessage
	public void OnMessage(String message, Session session){
		
		System.out.println(session.hashCode()+"说: "+message);
		//群发消息
        for(WebsocketServer item: webSocketSet){             
            try {
            	/*if(!item.equals(sionWebMap.get(session))){
            		item.sendMessage(message);
            	}*/
            	item.sendMessage(session.hashCode()+"说: "+message);
            	
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
	}
	@OnError
	public void OnError(Session session, Throwable throwable){
		System.out.println("发生错误");
		throwable.printStackTrace();
	}
	
	@OnClose  
	public void onClose(Session session, CloseReason reason){ 
		sessions.remove(session);
		webSocketSet.remove(this);
		sionWebMap.remove(session);
		System.out.println("连接关闭: "+session.hashCode());
	}
	
	  /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
 
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
    	WebsocketServer.onlineCount++;
    }
     
    public static synchronized void subOnlineCount() {
    	WebsocketServer.onlineCount--;
    }
}
