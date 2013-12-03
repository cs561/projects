var static = require('node-static');
var http = require('http');
var file = new(static.Server)();
var app = http.createServer(function (req, res) {
  file.serve(req, res);
}).listen(2013);

var nodes = [];
var nodeMap = {};

var io = require('socket.io').listen(app);

io.sockets.on('connection', function (socket){


  // convenience function to log server messages on the client
        function log(){
                var array = [">>> Message from server: "];
          for (var i = 0; i < arguments.length; i++) {
                array.push(arguments[i]);
          }
            //socket.emit('log', array);
			 console.log(array);
        }

        socket.on('status',function(evt){

            if(nodeMap[evt.id+""] == undefined){
                //new node

                console.log("new Node: " + evt.id);
                nodes.push(new node(socket, evt.id, evt.cons));
                nodeMap[evt.id+""] = true;


                socket.emit('candidateList', createCandidates());

            }else{
                //old node
                console.log("status from old node");
                for (var i = 01; i < nodes.length; i++) {
                    if (nodes[i].id == evt.id) {
                        console.log("update nodelist" + evt.id);
                        nodes[i].nrCons = evt.nrCons;
                        break;
                    }
                }

                if(evt.cons < 4){
                    socket.emit('candidateList', createCandidates());
                }

            }

        });

    function createCandidates(){
        var candidates = [];
        nodes.forEach(function(node){
            console.log('sending Candidate ' + node.id);
            candidates.push({id:node.id,nrCons:node.nrCons});
        })
        console.log("candidate list size: "+candidates.length);
        return candidates;
    }

		
		socket.on('disconnect', function(){
            for (var i = nodes.length-1; i >= 0; i--) {
                if (nodes[i].socket == socket) {
                    console.log("Removed node from list: "+nodes[i].id);
                    delete nodeMap[nodes[i].id];
                    nodes.splice(i, 1);
                    break;
                }
            }
		});

        socket.on('sendToNode', function(evt){
            socket.broadcast.emit('recvToNode',evt);
        });



});

node = function(_socket, _id, _connections){
    this.socket = _socket;
    this.id = _id;
    this.nrCons = _connections;
}