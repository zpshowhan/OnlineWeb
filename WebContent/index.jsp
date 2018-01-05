<%@ page language="java" import="javax.*,java.util.*" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>测试 websocket</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript">
var server;
//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
	server = new WebSocket("ws://localhost:8080/OnlineWeb/server/web");
}
else{
    alert('Not support websocket')
}
server.onerror = function(event) {
    onError(event)
  };

  server.onopen = function(event) {
    onOpen(event)
  };

  server.onmessage = function(event) {
    onMessage(event)
  };

  function onMessage(event) {
    document.getElementById('messages').innerHTML
      += "<div class='textleft'>"+event.data+"</div>" ;
  }

  function onOpen(event) {
    document.getElementById('messages').innerHTML
      = "<div class='textcenter'>连接已接通</div>";
  }

  function onError(event) {
    alert(event.data);
  }

  function start() {
	  var val=document.getElementById('info').value;
	  
	  document.getElementById('messages').innerHTML
      += "<div class='textright'>"+val+" 我说</div>" ;
      
	  if(val!=""&&val!=undefined){
	  server.send(val);
	  }
	  
	  document.getElementById('info').value="";
    return false;
  }
</script>
<style type="text/css">
#messages{
width: 500px;
height: 500px;
border: 1px solid blue;
}
.textleft{
width: 500px;
/* height: 50px; */
border: 0px solid blue;
text-align: left;
}
.textright{
width: 500px;
/* height: 50px; */
border: 0px solid blue;
text-align: right;
}
.textcenter{
width: 500px;
height: 50px;
border: 0px solid blue;
text-align: center;
}
#info{
width: 398px;
height: 40px;
border: 1px solid black;
}
.but{
width: 98px;
height: 40px;
border: 1px solid lightblue;
background-color: lightblue;
}
</style>
</head>
<body>
<div align="center">
  <div id="messages" align="left"></div>
    <input id ="info" type="text" />
    <button class="but" onclick="start()">发送</button>
    </div>
</body>
</html>