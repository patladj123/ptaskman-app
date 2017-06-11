//Initialize the websocket
var relativeRootWsProto=document.location.href.replace(/^http\:\/\//ig,'ws://').replace(/ptaskman\.jsp$/,'')+'../';
var ws = new WebSocket(relativeRootWsProto+"ptoperations");
ws.onopen = function(){
};
ws.onmessage = function(message){
    document.getElementById("chatlog").textContent += message.data + "\n";
};
function postToServer(){
    ws.send(document.getElementById("msg").value);
    document.getElementById("msg").value = "";
}
function closeConnect(){
    ws.close();
}

