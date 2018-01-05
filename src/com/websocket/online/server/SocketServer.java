package com.websocket.online.server;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.websocket.online.param.ParamUtil;

import net.sf.json.JSONObject;

@ServerEndpoint("/server/web")
public class SocketServer {

	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    
	private Session session;
	//线程安全的集合
	private static ConcurrentHashMap<Session,SocketServer> sionWebMap=new ConcurrentHashMap<Session,SocketServer>();
	
	@OnOpen
	public void OnOpen(Session session){
		this.session=session;
		if(!sionWebMap.containsKey(session)){
			sionWebMap.put(session, this);
			addOnlineCount();
		}
		System.out.println(session.hashCode()+" 加入聊天.当前人数 "+onlineCount);
	}
	
	@OnMessage
	public void OnMessage(String message, Session session){
		
		System.out.println(session.hashCode()+"说: "+message+"\n"+"当前人数： "+onlineCount);
		//这里最好加入判断不群发自己，自己所发的信息，是不会群发给自己的
		String resoult=ParamUtil.paramClint(message, "post", "qq");
		JSONObject json = JSONObject.fromObject(resoult);
		StringBuffer retext=new StringBuffer(json.getString("text"));
		//String retext=json.getString("text");
		//群发消息
		for(Entry<Session,SocketServer> entry : sionWebMap.entrySet()){
			try {
				if(entry.getKey().equals(session)){
					entry.getValue().sendMessage("机器人说: "+retext);
				}else{
					entry.getValue().sendMessage(session.hashCode()+"说："+message+"</br>机器人说: "+retext);
				}
            	
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
    	SocketServer.onlineCount++;
    }
     
    public static synchronized void subOnlineCount() {
    	SocketServer.onlineCount--;
    }
}
