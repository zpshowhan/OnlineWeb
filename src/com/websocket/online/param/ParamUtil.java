package com.websocket.online.param;

import java.util.HashMap;
import java.util.Map;

import com.websocket.online.utils.HTTP;
import com.websocket.online.utils.HttpXmlClient;
import com.websocket.online.utils.commonAPI;

import net.sf.json.JSONObject;

/**   
 * @Title: ParamUtil.java 
 * @Package com.websocket.online.param 
 * @Description: 信息交换工具类 默认使用post方式
 * @Company:esmart
 * @author admin  
 * @date 2016年8月19日 下午3:10:38 
 * @version V1.0   
 */
public class ParamUtil {

	public static String paramClint(String data,String http,String type){
		
		Map<String, String> params = new HashMap<String, String>();
		String result="";
		if(data!=""&&!data.equals("")){
			if(http.equals(HTTP.POST)){
				if(type.equals(HTTP.QQ)){
					params.put("key", commonAPI.QQRoot);
				}else{
					params.put("key", commonAPI.TLRoot);
				}
				params.put("info", data);
				result=HttpXmlClient.post(commonAPI.APIUrl, params);
			}else{
				String info=commonAPI.KEYStart;
				if(type.equals(HTTP.QQ)){
					info+=commonAPI.QQRoot;
				}else{
					info+=commonAPI.TLRoot;
				}
				result=HttpXmlClient.get(commonAPI.APIUrl+info+commonAPI.INFOStart+data);
			}
			System.out.println("返回消息为："+result);
			return result;
		}else{
			System.out.println("信息为空请检查");
			return "{'code':'-1','text':'信息为空请检查'}";
		}
	}
	public static void main(String[] args) {
		//String result =ParamUtil.paramClint("你好", "post", "qq");
		String result="{'code':'-1','text':'信息为空请检查'}";
		JSONObject json=JSONObject.fromObject(result);
		System.out.println(json.toString());
	}
}
