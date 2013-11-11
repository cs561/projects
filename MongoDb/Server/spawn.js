var sys = require('sys');
var exec = require('child_process').exec;
var spawn = require('child_process').spawn;
var http = require('http');
var readstream= require('readable-stream');
stream = new readstream("");
var child;	

var stream = require('stream').readable

var output=null;

console.log("executing ./mongo");
child = spawn("./mongo");





//process.stdin.pipe(child.stdin);
//child.stdout.pipe(process.stdout);
	
	

	
process.stdin.on('data', function(data){
	
	
	var string = data.toString().substr(1, data.toString().length-2);
	child.stdin.write(string);
	child.stdin.write(String.fromCharCode(10));
	});

/*
process.stdout.on('data', function(data){
	process.stdout.write("\rProcess schickt daten Process\r");
	process.stdout.write(data.toString());
	output=data.toString();

	});	*/	
//child.stdout.pipe(stream);

var	output="";
var sockets = {};
var globalSocketIdx = 0;

var io = require('socket.io').listen(8888);
io.set('log level', 1);

io.sockets.on('connection', function(socket) {
    var socketIdx = globalSocketIdx++;
    sockets[socketIdx] = socket;
    console.log("executing ./mongo");
	childforsock = spawn("./mongo");
	socket.child=childforsock;
	
    
    socket.child.stdout.on('data', function(data){
		console.log("-------------------");
		console.log(socket.handshake.address.address + ":");
		console.log("input:");
		console.log(socket.clientinput);
		
		
		console.log("output:");
		
		var output=data.toString();
		console.log(output);
		console.log("-------------------");
		socket.emit('data', output);
		socket.clientinput="";
	});
    
    
    
    socket.on('mirror', function(data) {
        console.log('Received mirror event');
        output=data;
        console.log(data);
		socket.emit('data', "{'"+output+"'}");
    });
	
    	
	
    socket.on('command', function(data) {
        
        
        var string = data.toString();//.substr(1, data.toString().length-2);
		socket.clientinput =string;
		if(string == "quit" || string == "quit()"){
				socket.emit('data', "quit is not supportet");
    
		}else{	
			socket.child.stdin.write(string);
			socket.child.stdin.write(String.fromCharCode(10));
		}
				 
        
		
    });


    socket.on('disconnect', function() {
        delete sockets[socketIdx];
    });
});

console.log('Mandelbrot socket.io server running at http://127.0.0.1:8888/');
