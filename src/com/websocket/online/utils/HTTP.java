package com.websocket.online.utils;

/** 
* @ClassName: http 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @Company:esmart
* @author Administrator 
* @version 1.0 2016年8月19日 下午3:14:02 
*/
public enum HTTP {

	POST("post"),GET("get"),TL("tl"),QQ("qq");
	private String name;
	
	
	private HTTP(String name){
		this.name=name;
	}
	@Override
    public String toString() {

        return String.valueOf(this.name);

    }
	public static void main(String[] args) {
		System.out.println(HTTP.POST);
	}
}
