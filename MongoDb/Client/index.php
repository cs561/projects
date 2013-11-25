<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <link rel="stylesheet" href="../css/ansi.css" type="text/css" media="all" />
    
    <style>
      html, body {
        background-color: #333;
        color: white;
        font-family: monospace;
        margin: 0;
        padding: 0;
      }
      #console {
        height: 600px;
        width: 1200px;
        position:relative;
        background-color: black;
        border: 2px solid #CCC;
        margin: 0 auto;
        margin-top: 50px;
      }
      .jqconsole {
        padding: 10px;
        padding-bottom: 10px;
      }
      .jqconsole-cursor {
        background-color: #999;
      }
      .jqconsole-blurred .jqconsole-cursor {
        background-color: #666;
      }
      .jqconsole-prompt {
        color: #0d0;
      }
      .jqconsole-old-prompt {
        color: #0b0;
        font-weight: normal;
      }
      .jqconsole-input {
        color: #dd0;
      }
      .jqconsole-old-input {
        color: #bb0;
        font-weight: normal;
      }
      .brace {
        color: #00FFFF;
      }
      .paran {
        color: #FF00FF;
      }
      .bracket {
        color: #FFFF00;
      }
      .jqconsole-composition {
        background-color: red;
      }
    </style>
  </head>
  <body>
    <div id="console"></div>
    <script src="jQuery.js" type='text/javascript'></script>
    <script type="text/javascript" src="http://<?php echo $_SERVER['SERVER_ADDR']; ?>:8888/socket.io/socket.io.js"></script>
	<script src="jqconsole.js"></script>
    
	
       <script>
	var socket = io.connect('http://<?php echo $_SERVER['SERVER_ADDR']; ?>:8888');

				// set a callback for the "data" event sent by the server
	socket.on('data', function(data) {
		console.log("recieved data");		
		console.log(data);
		jqconsole.Write("\n");
		jqconsole.Write(data);
		jqconsole.Write("\n");
	});	   
	
	
	var Mirror = function (command){
		console.log(command);
		
		socket.emit('mirror', JSON.stringify(command));
		};	   
	var SendCommand = function (command){
		console.log(command);
		
		socket.emit('command', command);
		};
		
      $(function() {
        // Creating the console.
        var header = 'Welcome to JQConsole!\n' +
                     'Use jqconsole.Write() to write and ' +
                     'jqconsole.Input() to read.\n';
        window.jqconsole = $('#console').jqconsole(header, 'MongoDB> ');

        // Abort prompt on Ctrl+Z.
        jqconsole.RegisterShortcut('Z', function() {
          jqconsole.AbortPrompt();
          handler();
        });
        // Move to line start Ctrl+A.
        jqconsole.RegisterShortcut('A', function() {
          jqconsole.MoveToStart();
          handler();
        });
        // Move to line end Ctrl+E.
        jqconsole.RegisterShortcut('E', function() {
          jqconsole.MoveToEnd();
          handler();
        });
        jqconsole.RegisterMatching('{', '}', 'brace');
        jqconsole.RegisterMatching('(', ')', 'paran');
        jqconsole.RegisterMatching('[', ']', 'bracket');
        // Handle a command.
        var handler = function(command) {
			    
			
          if (command) {
			var response = SendCommand(command);

            try {
              //jqconsole.Write('==> ' + window.eval(command) + '\n');
              //jqconsole.Write("hello");
            } catch (e) {
              //jqconsole.Write('ERROR: ' + e.message + '\n');
              //jqconsole.Write("otherhello");
            }
          }
          jqconsole.Prompt(true, handler, function(command) {
            // Continue line if can't compile the command.
            try {
              console.log(response);
            } catch (e) {
              if (/[\[\{\(]$/.test(command)) {
                return 1;
              } else {
                return 0;
              }
            }
            return false;
          });
        
        };
		
        // Initiate the first prompt.
        handler();
      });
  
    </script>
   
  </body>
</html>
