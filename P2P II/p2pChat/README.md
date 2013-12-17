# p2p Chat Application

A proof of concept for a browser based peer to peer chat.
The peer to peer connections are realised with webRTC using datachannels. 
A server is still needed to establish a connection between two peers.
After the connection is established, the server is not needed (you can shutdown
the server and still chat).

## Server
Instructions how to get the server running in Debian Linux/GNU

1. Install NodeJS (get it from the unstable repository)
2. Checkout the source
3. 'cd /path/to/source'
4. Install all needed dependencies with 'npm install'
5. Run the server with 'node server.js'
6. Your server will now be listening to on 'localhost:2013'


## Browser
Requirements: you need chrome/chromium v31.
Browse to localhost:2013 and chat.
