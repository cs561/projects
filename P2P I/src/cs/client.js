
  //
  // Server/Client-Modell: Client.
  // Uni Basel, HS 1013.
  // Seminar: Verteilte Systeme.
  //

// Configuration
var nconf = require('nconf');
nconf.file({ file: 'config.json' });
nconf.defaults(
    {
        "serverip": "localhost",
        "serverport": 5000,
        "collectorip": "localhost",
        "collectorport": 3000,
    }
);

// Requirements
var io  = require('socket.io-client');
var dl  = require('delivery');
var fs  = require('fs');

// Prepare communication to Collector
var http = require('http');
function msgCollector(m){
    var req = http.get("http://" + nconf.get("collectorip") + ":" + nconf.get("collectorport") + "/msg/" + m, function(res){} );

    req.end();
};

function statCollector(n, t){
    var req = http.get("http://" + nconf.get("collectorip") + ":" + nconf.get("collectorport") + "/cs/" + n +  "/" + t);
    req.end();
}

// Client name
if(process.argv[2] == undefined){
    console.log('No name specified.');
    console.log('Usage:');
    console.log('  node client name');
    process.exit(1);
}
var name = process.argv[2];

// Request file
msgCollector("Client " + name + " started. Request file...");
var start_time = new Date().getTime();
var socket = io.connect('http://localhost:5000');

socket.on('connect', function(){

    var delivery = dl.listen(socket);
    delivery.connect();

    delivery.on('receive.success',function(file){

        fs.writeFile(file.name, file.buffer, function(err) {
            if(err) {
                msgCollector("Client " + name + ": Transfer failed!");
                console.log("Transfer failed.");
            } else {
                msgCollector("Client " + name + ": Transfer successful!");
                console.log("Transfer successful.");
                end_time = new Date().getTime();
                statCollector(name,end_time-start_time);
            };
        });
    });
});




