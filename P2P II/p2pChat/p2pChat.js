//shall i be verbose or not?
var VERBOSE = true;

/**
 * Class holding one chat message
 * @param _user
 * @param _message
 * @param _type
 * @constructor
 */
var Message = function(_user, _message, _type){
    this.user = _user;
    this.message = _message;
    this.mType = _type;
}

/**
 * Implements a simple chat GUI with all the listeners etc. needed
 * @param chatWindow the DOM element to be a chat
 * @param messageInput where the users inputs data
 * @param sendButton ~
 * @constructor
 */
var ChatBox = function(chatWindow, messageInput, sendButton){

    var SELF = this;
    var maxMsg = 15;
    var msgFIFO = [];
    var chatWindow = $(chatWindow);
    var messageInput = $(messageInput);
    var sendButton = $(sendButton);

    var nick = "John Cleese";
    var nicks = ["john", "hans", "peter", "Eric Idle", "John Cleese", "King Arthur", "James Gosling", "fox","cat", "frog", "John Doe", "Jane Doe",
        "Agent Smith", "Neo", "ECMA", "toblerone", "Brian", "Bruce"];
    nick = nicks[Math.floor(Math.random()*nicks.length)];

    /**
     * prints a message in the chat
     * @param message an Object of the Class Message to be printed
     */
    function emit(message){
        //console.log('New Message: '+user+" wrote '"+msg+"'");
        if( nick != "root"){
            //todo this doesn't work
            message.message.replace(/<\/?(iframe|button|script|span|div|img|h1|h2|h3|h4|p)\b[^<>]*>/g, "")

        }
        if(message.message == ""){return;}

        var msgElem = new HTMLMessage(message);
        console.log(msgElem);
        msgFIFO.push(msgElem);
        chatWindow.append(msgElem);
        if(msgFIFO.length >= maxMsg){
            var oldestMsg = msgFIFO.shift()
            removeElement(oldestMsg);
        }
    }

    /**
     * Makes a html DOM-Element representing the Message
     * @param message Message
     * @returns {HTMLElement}
     * @constructor
     */
    var HTMLMessage = function (message){
        var p = document.createElement('p');
        var usrH = document.createElement('span');
        var msgH = document.createElement('span');
        $(usrH).addClass('username');
        $(msgH).addClass('message');
        $(usrH).html(message.user);
        $(msgH).html(message.message);
        p.appendChild(usrH);
        p.appendChild(msgH);
        if( message.mType){
            $(p).addClass( message.mType);
        }
        return p;
    }

    /**
     * removes the Node from the DOM
     * @param node
     */
    function removeElement(node) {
        node.parentNode.removeChild(node);
    }

    /**
     * Parse a Message in the Inputfield and do something with it
     */
    function parseMessage(){
        var msg = messageInput.val();

        //check if empty
        if(msg.length <= 0){
            return;
        }

        //check for command
        if(msg.substr(0,1) === "/"){
            //is a command
            var cmd = msg.substring(1,msg.indexOf(" "));
            var argument = msg.slice(msg.indexOf(" "),msg.length);
            console.log("parseMessage: received Command: "+cmd+ " with arg:"+argument);
            switch(cmd){
                case "nick":
                    if(argument.length <=4){
                        emit(new Message("System", "sorry, too short", 'error'));
                        break;
                    }

                    var oldNick = nick;
                    nick = argument;
                    emit(new Message("System", "\You changed your nick to: \<i>"+nick+"\</i>", 'system'));
                    SELF.onMessage(new Message("System","\<i>"+oldNick+"\</i> changed his name to <i>"+nick+"\</i>", 'system'));
                    break;

                case "me":
                    emit(new Message("", "\<em>"+nick+"\</em> "+argument, ''));
                    SELF.onMessage(new Message("", "\<em>"+nick+"\</em> "+argument, ''));
                    break;
            }


        }else{
            //is no command, send it
            emit(new Message(nick, msg));
            SELF.onMessage(new Message(nick, msg));
        }

        messageInput.val("");
    }

    /**
     * Attach Handler to Enter-Key and sendButton
     */
    sendButton.click(function(){parseMessage();});
    messageInput.keydown(function(e){
        if(e.keyCode == 13){
            parseMessage();
        }
    });


    /**
     * Export the emit function allowing external input to be printed
     * @type {Function}
     */
    this.emitMsg = emit;

    /**
     * This is a function hook executed if the user enters a new message
     */
    this.onMessage = function(message){}


}

/**
 * Overlay for the WEBRTC-Connection. Abstracts many function allowing easier handling
 * in the ConnectionHandler
 *
 * @param _myId the Id of this Node
 * @param _remoteId the Id of the node at the other end
 * @param _signalingChannel signaling channel to use
 * @constructor
 */
var RTCConnection = function(_myId, _remoteId, _signalingChannel){


    //todo implement timeout for closing connection

    /**
     * Eventhandlers called by this class:
     */
    this.onChatMsg = function(){}; //called if chatmessage arrives
    this.onCmdMsg = function(){}; //called if command arrives
    this.onOpen = function(){}; //called if connection is open
    this.onClose = function(){};

    /**
     * Status of this Connection
     */
    this.isOpen = false;
    this.isActive = true;


    //public members/functions
    this.getRemoteId = function(){return remoteId};
    this.sendChat = function(msg){if(SELF.isOpen){sendJSON(chatChannel, msg)}};
    this.sendCmd = function(msg){if(this.isOpen){sendJSON(cmdChannel, msg)}};

    /**
     * Hooks which can be overwritten:
     */
    this.signalingChannel = _signalingChannel;


    //constants
    var SELF = this;
    var CONDEBUG = false; //if this class is verbose
    var TIMEOUT = 60000; //when a connection is considered closed

    //some vars
    var myId = _myId;
    var remoteId = _remoteId;

    //open connection
    var configuration = { "iceServers": [{ "url": "stun:23.21.150.121" }] };
    var rtcCon = new RTCPeerConnection(configuration, null);
    var openHandlerCalled = false;
    var pingTime;
    var pongTime = 0;

    //make channels
    var cmdIsOpen=false, chatIsOpen=false, serviceIsReady = false;
    var cmdChannel = rtcCon.createDataChannel("cmdChannel", null);
    var chatChannel = rtcCon.createDataChannel("chatChannel", null);
    var serviceChannel = rtcCon.createDataChannel("serviceChannel", null);

    //attach Channel handlers
    cmdChannel.onopen = function(){cmdIsOpen = true; openHandler(); console.log("commandChannel open "+remoteId)};
    cmdChannel.onmessage = function(msg){
        //todo do something here
        CONDEBUG && console.log("Received Command:" + msg);
        CONDEBUG && console.log(msg);

        SELF.onCmdMsg(parseJSON(msg.data));
    };

    chatChannel.onopen = function(){chatIsOpen = true; openHandler(); console.log("chatChannel open "+remoteId)};
    chatChannel.onmessage = function(msg){
        //todo do something here
        console.log("Received Chat:" + msg);
        console.log(msg);

        SELF.onChatMsg(parseJSON(msg.data));
    };

    serviceChannel.onopen = function(){sendJSON(serviceChannel, {type:"conDiscovery", senderId: myId});}
    serviceChannel.onmessage = function(msg){

        var message = parseJSON(msg.data);
        if(typeof message === 'undefined'){return;}

        //implements some logic because the service channel is only for us
        switch(message.type){
            case "conDiscovery":
                SELF.remoteId = message.senderId;
                serviceIsReady = true;
                openHandler();
                break;
            case "ping":
                CONDEBUG && console.log(myId + " received a ping from "+remoteId);
                var t = new Date();
                sendJSON(serviceChannel, {type:"pong", time: t.getTime()});
                break;
            case "pong":
                pongTime = message.time-pingTime;
                CONDEBUG && console.log(myId + " pong from "+remoteId+ " rtt: "+pongTime);
        }
    }

    /**
     * Ping function to observe the connection status
     * Call this at least 2-3 in the time specified by TIMEOUT;
     */
    this.ping = function(){
        if( !SELF.isOpen && SELF.isActive){return;}
        if(pongTime > TIMEOUT || !SELF.isActive){
            SELF.isActive = false;
            SELF.isOpen = false;
            this.onClose();
        }

        var date = new Date();
        pingTime = date.getTime();
        pongTime = 2*TIMEOUT;
        sendJSON(serviceChannel, {type:"ping", time: pingTime});
    }


    /*
     * Now we have all Datachannels created, let's attach ICE and STUN handlers to
     * the connection
     */
    rtcCon.onicecandidate = function (evt) {
        if (evt.candidate){
            VERBOSE && CONDEBUG && console.log(evt.candidate);
            var candidate={type: 'candidate',
                sdpMLineIndex: evt.candidate.sdpMLineIndex,
                candidate: evt.candidate.candidate};
            if(SELF.signalingChannel){SELF.signalingChannel(candidate)};
            VERBOSE && CONDEBUG && console.log('candidate sent');
        } else{
            VERBOSE && CONDEBUG && console.log("End of candidates.");
        }
    };

    //specified but never called
    rtcCon.ondatachannel = function(event) {
        console.log('found datachannel ===========================================');
        receiveChannel = event.channel;
        receiveChannel.onmessage = function(event){
            //FIXME
        };
    };

    /**
     * Tries to send a javascript Object as JSON over the specified Datachannel
     * @param channel
     * @param data
     */
    function sendJSON(channel, data){
        CONDEBUG && console.log("sendJSON: send message");
        try{
            channel.send(JSON.stringify(data));
        }catch(e){

        }
    }

    /**
     * Parses received JSON to an Object
     * @param data
     * @returns {*}
     */
    function parseJSON(data){
        try{
            return JSON.parse(data);
        }catch(e){

        }
    }

    /**
     * creates a webRTC session offer
     */
    function createOffer(){
        rtcCon.createOffer(setLocalDescAndEmit, logError,null);
        console.log('created Offer');
    }

    /**
     * Set the localdescription and emit it through the signalling channel;
     * @param desc
     */
    function setLocalDescAndEmit(desc) {
        rtcCon.setLocalDescription(desc, function () {
            SELF.signalingChannel(desc);
        }, logError);
    }

    /**
     * Function called if we receive a signal through the signal channel
     * @param signal
     */
    function receiveSignal(signal){
        if(signal.type){
            CONDEBUG && console.log("receiveSignal: received "+signal.type);
        }else{
            CONDEBUG && console.error("receiveSignal: received signal with no type");
        }

        if(signal.type==='offer'){
            CONDEBUG && console.log('receiveSignal: received Offer');
            var remoteDesc=new RTCSessionDescription(signal);
            CONDEBUG && console.log(remoteDesc);
            rtcCon.setRemoteDescription(remoteDesc,function(){
                console.log('Sending answer...');
                rtcCon.createAnswer(setLocalDescAndEmit, logError,null);
            });

        } else if(signal.type==='answer'){
            CONDEBUG && console.log('receiveSignal: received Answer');
            var remoteDesc=new RTCSessionDescription(signal);
            CONDEBUG && console.log(remoteDesc);
            rtcCon.setRemoteDescription(remoteDesc);

        } else if(signal.type==='candidate'){
            CONDEBUG && console.log('receiveSignal: received ICE Candidate');

            var candidate = new RTCIceCandidate(signal);
            CONDEBUG && console.log(candidate);
            rtcCon.addIceCandidate(candidate);
        }
    }

    function logError(error) {
        console.log(error.name + ": " + error.message);
        console.error(error);
    }

    /**
     * handler to determine if the connection is open,
     * if so this.open will be set to true
     */
    function openHandler(){
        if(cmdIsOpen && chatIsOpen && serviceIsReady){
            CONDEBUG && console.log("openHandler: Connection established");
            SELF.isOpen = true;
            try{
                if(openHandlerCalled == false){
                    SELF.onOpen();
                    openHandlerCalled = true;
                }
            }catch(e){
                //well
            }
        }
    }

    /**
     * export some properties/functions to be public
     */
    this.createOffer = createOffer;
    this.receivedSignal = receiveSignal;
    this.remoteId = remoteId;
    this.myId = myId;
};//end RTCConnection

/**
 * Represents a node in the network
 * @param _nodeId
 * @param _nrCons
 * @constructor
 */
var Node = function(_nodeId, _nrCons){
    this.id = _nodeId;
    this.nrCons = _nrCons;
}

/**
 * Handles all the connection made by this Node. Builds an overlay
 * through which communication is possible. Implements all the logic needed
 * for our simple broadcast to all routing.
 * @param param objects containing the signalingchannels, chat etc.
 * @constructor
 */
var ConnectionHandler = function(param){
    var SELF = this;
    var HANDLERVERB = false; //if this class is verbose

    var myNodeId =  Math.floor(Math.random()*1000000); //generate our id
    var maxConnections = 3; //how many connections shall made actively
    var candidates = []; //connection candidates
    var connectionsMap = [];
    var connections = [];

    //public properties
    this.statusChan = param.statusChan;
    this.establishChan = param.establishChan;
    this.nodeId = myNodeId;
    this.connections = connections;

    //some hook
    this.onSystemMsg = function(msg){}

    /**
     * called when a connection changes, fires the conChangeListener and emits a message over the
     * statusChan
     */
    this.sendStatus = function(){
        HANDLERVERB && console.log("sendStatus: Updating Status, we have now "+connections.length+" connections");
        SELF.conChangeListener(connections);
        SELF.statusChan({id:myNodeId, cons:connections.length});
    }

    /**
     * Processes a list of candidates and tries to connect to them
     * @param signal
     */
    this.processCandidates = function(signal){
        candidates = [];

        //make list
        for(var i=0;i<signal.length;i++){
            candidates.push(new Node(signal[i].id, signal[i].nrCons));
        }
        //sort so we have the ones with the least nr. of connections first
        candidates.sort(function(nodeA, nodeB){
            return nodeA.nrCons - nodeB.nrCons;
        });

        //try to establish connection if we don't have enough already
        for(var i=0; i < candidates.length && connections.length < maxConnections; i++){
            if(candidates[i] == undefined){continue;};

            var node = candidates[i];
            if(node.id != myNodeId && connectionsMap[node.id] == undefined){
                establishConnection(myNodeId, node.id, true);
            }
        }
    }

    /**
     * handler to receive a signal over the signalling channel.
     * Either directs it to the correct connection or open a new connection and then
     * relay the message to this new connection.
     * @param signal
     */
    this.receiveSignal = function(signal){
        if(signal.dest == myNodeId){
            if(connectionsMap[signal.src]){
                connectionsMap[signal.src].receivedSignal(signal.data);
            }else{
                HANDLERVERB && console.log("receiveSignal: someonewants to connect to us: "+signal.src);
                establishConnection(myNodeId, signal.src, false);
                connectionsMap[signal.src].receivedSignal(signal.data);
            }
        }
    }

    /**
     * Closure to wrap every signal emitted by a connection in a Packet
     * @param _id
     * @returns {Function}
     */
    var makeSignalingChan = function(_id){
        function signalingChan(signal){
            SELF.establishChan({dest:_id, src:myNodeId, data:signal});
        };
        return signalingChan;
    }

    /**
     * Creates and initializes a new Connection.
     * If createOffer is set, the connection will try to connect to its counterpart
     * @param myId
     * @param remoteId
     * @param createOffer
     */
    function establishConnection(myId, remoteId, createOffer){
        var con = new RTCConnection(myId, remoteId, makeSignalingChan(remoteId));
        connectionsMap[remoteId] = con;
        connections.push(con);

        //attach Handlers to connection
        con.onOpen = function(){
            SELF.onSystemMsg("\Connection established to peer <em>" + con.remoteId + "\</em>");
            SELF.sendStatus();
        }

        con.onClose = function(){
            SELF.onSystemMsg("\Connection has been lost to peer <em>" + con.remoteId + "\</em>");
            connectionsMap[con.remoteId] = undefined;
            for (var i = connections.length-1; i >= 0; i--) {
                if (connections[i].remoteId == con.remoteId) {
                    connections.splice(i, 1);
                    break;
                }
            }
            SELF.sendStatus();
        }

        //attach cmd Handler
        con.onCmdMsg = function(msg){
            if(msg == undefined){return;}
            try{
                receiveCMDPacket(msg);
            }catch(e){
                //something went wrong
            }
        }

        //attach chat Handler
        con.onChatMsg = function(msg){
            HANDLERVERB && console.log("onchatMsg Handler received");
            try{
                receiveChatPacket(msg);
            }catch (e){
                //no valid message
            }
        };

        if(createOffer){
            con.createOffer();
        }
    }

    /**
     * executes ping() of every connection so we know when a connection is closed
     */
    var checkActive = function(){
        connections.forEach(function(con){
            con.ping();
        })
    }

    /*
     Call checkactive regularly, con Timeout is 60s, we will check 6 times before timeout
     */
    setInterval(function(){checkActive();}, 10000); //todo enable me

    /**
     *  =====================================================================================
     *
     *                           Packet Stack & Communication
     *
     *  ======================================================================================
     */

        //hooks to be overwritten
    this.onChatMsg = function(msg){};
    this.onCmdMsg = function(){};
    this.conChangeListener = function(){};
    this.receivedCMDPacket = function(){};

    var chatMap = {};
    var cmdMap = {};

    /**
     * sends a chat packet in the network
     * @param _msg
     */
    this.sendChatPacket = function(_msg){
        HANDLERVERB && console.log("Sending new chat packet");
        var pkt = new Packet(_msg, new Date(), myNodeId);
        broadcastChatPacket(pkt);
    }

    /**
     * receives a chat packet from the network
     * @param _pkt
     */
    var receiveChatPacket = function(_pkt){
        HANDLERVERB && console.log("receiveChatPacket");
        HANDLERVERB && console.log(_pkt);

        broadcastChatPacket(_pkt, function(pkt){
            SELF.onChatPkt(pkt.msg);
        });
    }

    /**
     * If the pkt is not known:
     * -Broadcast it to all
     * -call callback
     * -add it to hashmap
     * Otherwise:
     * -do nothing
     * @param pkt
     * @param newCallback
     */
    function broadcastChatPacket(pkt, newCallback){
        HANDLERVERB && console.log(pkt);
        HANDLERVERB && console.log("package to resend: sending hash " + pkt.hash);

        if(chatMap[pkt.hash] == undefined){
            HANDLERVERB && console.log("new message sending to all connections");
            if(typeof newCallback == 'function'){
                newCallback(pkt);
            }

            chatMap[pkt.hash] = pkt;
            connections.forEach(function(con){
                con.sendChat(pkt);
            });
        }else{
            HANDLERVERB && console.log('package already known');
        }
    }


    /*
     and now for something completely different:
     THE SAME
     (whoha code duplication)
     */
    this.sendCMDPacket = function(_msg){
        HANDLERVERB && console.log("Sending new cmd packet");
        var pkt = new Packet(_msg, new Date(), myNodeId);

        broadcastCMDPacket(pkt);
    }
    var receiveCMDPacket = function(_pkt){
        HANDLERVERB && console.log("receiveCMDPacket");
        HANDLERVERB && console.log(_pkt);

        broadcastCMDPacket(_pkt, function(pkt){
            SELF.receivedCMDPacket(pkt.msg);
        });
    }
    function broadcastCMDPacket(pkt, newCallback){
        HANDLERVERB && console.log(pkt);
        HANDLERVERB && console.log("CMD package to resend: sending hash " + pkt.hash);
        if(cmdMap[pkt.hash] == undefined){
            HANDLERVERB && console.log("CMD new package sending to all connections");
            if(typeof newCallback == 'function'){
                newCallback(pkt);
            }

            cmdMap[pkt.hash] = pkt;
            connections.forEach(function(con){
                con.sendCmd(pkt);
            });
        }else{
            HANDLERVERB && console.log('CMD package already known');
        }
    }

    /**
     * A packet to send over the network. Will generate it's hash out
     * of the time, dest Nr. and Src. Nr.
     * (Using MD5 so we might have collisions, idc )
     * @param _msg
     * @param _date
     * @param _myId
     * @param _destId
     * @constructor
     */
    var Packet = function(_msg, _date, _myId, _destId){
        this.src = _myId;
        this.dest = (_destId == undefined) ? 0: _destId;
        this.msg = _msg;
        this.time = _date.getTime();
        this.hash = CryptoJS.MD5(this.time+"+"+_myId+"+"+_destId+"+").toString(CryptoJS.enc.Hex);
    }

    /**
     *  clears the hashtables of hashes older than the timeout
     */
    var clearHashTables = function(timeout){
        var tTime = (new Date()).getTime()

        for (var msg in chatMap) {
            if(chatMap.hasOwnProperty(msg)){
                if(tTime-msg.time > timeout){
                    delete chatMap.msg;
                }
            }
        }
        for (var msg in cmdMap) {
            if(cmdMap.hasOwnProperty(msg)){
                if(tTime-msg.time > timeout){
                    delete cmdMap.msg;
                }
            }
        }
    }

    //call this regularly
    setInterval(function(){clearHashTables(5000);},3000);

}